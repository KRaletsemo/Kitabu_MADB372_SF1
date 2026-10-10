package com.example.kitabusf1.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kitabusf1.data.BookingStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//This file is used to test Room which uses "AndroidTest"
@RunWith(AndroidJUnit4::class)
class LibraryDaoTest {
// "lateinit var", means things will be set up before getting used
    private lateinit var db: AppDatabase
    private lateinit var bookDao: BookDao
    private lateinit var bookingDao: BookingDao

    private val day = 24 * 60 * 60 * 1000L

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder( //uses to build db in memory only
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        bookDao = db.bookDao()
        bookingDao = db.bookingDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private suspend fun addBook(title: String, author: String = "Author"): Int =
        bookDao.insertBook(BookEntity(title = title, author = author, category = "Fiction")).toInt()

    @Test
    fun insertedBookIsAvailableByDefault() = runBlocking {
        addBook("Disgrace")
        val books = bookDao.getAllBooks().first()
        assertEquals(1, books.size)
        assertTrue(books.first().isAvailable)
    }

    //From here the actual testing starts, Assertions are used to do these checks and say where the error is if there is one

    @Test
    fun searchMatchesTitleOrAuthorIgnoringCase() = runBlocking {
        addBook("Born a Crime", "Trevor Noah")
        addBook("Disgrace", "J.M. Coetzee")
        assertEquals(1, bookDao.searchBooks("noah").first().size)
        assertEquals(1, bookDao.searchBooks("DISG").first().size)
        assertEquals(2, bookDao.searchBooks("").first().size)
        assertEquals(0, bookDao.searchBooks("xyz").first().size)
    }

    @Test
    fun reserveMakesBookUnavailableAndAddsPendingBooking() = runBlocking {
        val id = addBook("Disgrace")
        assertTrue(bookingDao.reserveBook(id, "Student", 0L, 14 * day))

        assertEquals(0, bookDao.getBooksByAvailability(true).first().size)
        val current = bookingDao.getCurrentBookings().first()
        assertEquals(1, current.size)
        assertEquals(BookingStatus.PENDING, current.first().booking.status)
        assertEquals("Disgrace", current.first().book.title)
    }

    @Test
    fun cannotReserveSameBookTwice() = runBlocking {
        val id = addBook("Disgrace")
        assertTrue(bookingDao.reserveBook(id, "Student", 0L, day))
        assertFalse(bookingDao.reserveBook(id, "Student", 0L, day))
        assertEquals(1, bookingDao.getCurrentBookings().first().size)
    }

    @Test
    fun collectThenRenewThenReturn() = runBlocking {
        val id = addBook("Disgrace")
        bookingDao.reserveBook(id, "Student", 0L, 14 * day)
        val bookingId = bookingDao.getCurrentBookings().first().first().booking.bookingId

        assertEquals(0, bookingDao.renewBooking(bookingId, 7 * day))
        assertFalse(bookingDao.returnBooking(bookingId))

        assertEquals(1, bookingDao.collectBooking(bookingId))
        assertEquals(1, bookingDao.renewBooking(bookingId, 7 * day))
        assertEquals(21 * day, bookingDao.getBooking(bookingId)!!.returnDeadline)

        assertTrue(bookingDao.returnBooking(bookingId))
        assertEquals(BookingStatus.RETURNED, bookingDao.getBooking(bookingId)!!.status)
        assertEquals(0, bookingDao.getCurrentBookings().first().size)
        assertTrue(bookDao.getAllBooks().first().first().isAvailable)
    }

    @Test
    fun cancelDeletesPendingBookingAndFreesBook() = runBlocking {
        val id = addBook("Disgrace")
        bookingDao.reserveBook(id, "Student", 0L, day)
        val bookingId = bookingDao.getCurrentBookings().first().first().booking.bookingId

        assertTrue(bookingDao.cancelBooking(bookingId))
        assertNull(bookingDao.getBooking(bookingId))
        assertTrue(bookDao.getAllBooks().first().first().isAvailable)
    }

    @Test
    fun cannotCancelActiveBooking() = runBlocking {
        val id = addBook("Disgrace")
        bookingDao.reserveBook(id, "Student", 0L, day)
        val bookingId = bookingDao.getCurrentBookings().first().first().booking.bookingId
        bookingDao.collectBooking(bookingId)

        assertFalse(bookingDao.cancelBooking(bookingId))
        assertEquals(1, bookingDao.getCurrentBookings().first().size)
    }

    @Test
    fun currentBookingsAreSortedByDeadline() = runBlocking {
        val a = addBook("A")
        val b = addBook("B")
        bookingDao.reserveBook(a, "Student", 0L, 10 * day)
        bookingDao.reserveBook(b, "Student", 0L, 2 * day)
        assertEquals(listOf("B", "A"), bookingDao.getCurrentBookings().first().map { it.book.title })
    }

    @Test
    fun seedAddsSampleBooksAndBookings() = runBlocking {
        AppDatabase.seed(db)

        assertEquals(10, bookDao.getAllBooks().first().size)
        assertEquals(6, bookDao.getBooksByAvailability(true).first().size)

        val current = bookingDao.getCurrentBookings().first()
        assertEquals(4, current.size)
        assertEquals("Disgrace", current.first().book.title)
    }
}