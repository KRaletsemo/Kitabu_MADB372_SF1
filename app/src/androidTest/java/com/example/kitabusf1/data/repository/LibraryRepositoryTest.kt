package com.example.kitabusf1.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kitabusf1.data.local.AppDatabase
import com.example.kitabusf1.data.local.BookEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
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
        db.bookDao().insertBook(BookEntity(title = title, author = author, category = "Fiction"))
    }

    private suspend fun titlesFor(query: String): List<String> =
        repository.getBooks(query).first().map { it.title }

    @Test
    fun searchIgnoresSpacesAroundTheText() = runBlocking {
        addBook("Disgrace")
        addBook("Born a Crime")
        assertEquals(listOf("Disgrace"), titlesFor("  disgrace  "))
    }

    @Test
    fun percentAndUnderscoreAreNotWildcards() = runBlocking {
        addBook("Disgrace")
        addBook("Born a Crime")
        assertEquals(emptyList<String>(), titlesFor("%"))
        assertEquals(emptyList<String>(), titlesFor("_"))
        assertEquals(emptyList<String>(), titlesFor("D_sgrace"))
    }

    @Test
    fun percentAndUnderscoreCanStillBeSearchedFor() = runBlocking {
        addBook("100% Wolf")
        addBook("snake_case")
        addBook("Disgrace")
        assertEquals(listOf("100% Wolf"), titlesFor("0%"))
        assertEquals(listOf("snake_case"), titlesFor("e_c"))
    }

    @Test
    fun emptySearchReturnsEveryBook() = runBlocking {
        addBook("Disgrace")
        addBook("Born a Crime")
        assertEquals(2, titlesFor("").size)
        assertEquals(2, titlesFor("   ").size)
    }
}
