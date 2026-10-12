package com.example.kitabusf1.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kitabusf1.data.local.AppDatabase
import com.example.kitabusf1.data.local.BookEntity
import com.example.kitabusf1.domain.DAY_MS
import com.example.kitabusf1.domain.RENTAL_OPTIONS
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LibraryRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: LibraryRepository

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        repository = LibraryRepository(db.bookDao(), db.bookingDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    private suspend fun addBook(title: String, author: String = "Author") {
        db.bookDao().insertBook(BookEntity(title = title, author = author, category = "Motorsport Engineering"))
    }

    private suspend fun titlesFor(query: String): List<String> =
        repository.getBooks(query).first().map { it.title }

    @Test
    fun searchIgnoresSpacesAroundTheText() = runBlocking {
        addBook("Race Car Vehicle Dynamics")
        addBook("Cybersecurity for Beginners")
        assertEquals(listOf("Race Car Vehicle Dynamics"), titlesFor("  race car vehicle dynamics  "))
    }

    @Test
    fun percentAndUnderscoreAreNotWildcards() = runBlocking {
        addBook("Race Car Vehicle Dynamics")
        addBook("Cybersecurity for Beginners")
        assertEquals(emptyList<String>(), titlesFor("%"))
        assertEquals(emptyList<String>(), titlesFor("_"))
        assertEquals(emptyList<String>(), titlesFor("R_ce Car Vehicle Dynamics"))
    }

    @Test
    fun percentAndUnderscoreCanStillBeSearchedFor() = runBlocking {
        addBook("100% Secure")
        addBook("snake_case")
        addBook("Race Car Vehicle Dynamics")
        assertEquals(listOf("100% Secure"), titlesFor("0%"))
        assertEquals(listOf("snake_case"), titlesFor("e_c"))
    }

    @Test
    fun emptySearchReturnsEveryBook() = runBlocking {
        addBook("Race Car Vehicle Dynamics")
        addBook("Cybersecurity for Beginners")
        assertEquals(2, titlesFor("").size)
        assertEquals(2, titlesFor("   ").size)
    }

    @Test
    fun reservationRejectsRentalLengthsThatAreNotOffered() = runBlocking {
        addBook("Race Car Vehicle Dynamics")
        val bookId = repository.getBooks().first().first().id

        for (days in listOf(0, -7, 8, 365)) {
            assertFalse(repository.reserveBook(bookId, days))
        }

        assertTrue(repository.getBooks().first().first().isAvailable)
        assertEquals(0, repository.getCurrentBookings().first().size)
    }

    @Test
    fun reservationSetsFutureDeadlineForEachOfferedLength() = runBlocking {
        for (days in RENTAL_OPTIONS) {
            addBook("Book $days")
            val bookId = repository.getBooks("Book $days").first().first().id
            val before = System.currentTimeMillis()

            assertTrue(repository.reserveBook(bookId, days))

            val booking = repository.getCurrentBookings().first().first { it.book.id == bookId }
            assertTrue(booking.returnDeadline >= before + days * DAY_MS)
            assertTrue(booking.returnDeadline > booking.bookingDate)
        }
    }
}
