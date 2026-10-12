package com.example.kitabusf1.data.repository

import com.example.kitabusf1.data.local.BookDao
import com.example.kitabusf1.data.local.BookEntity
import com.example.kitabusf1.data.local.BookingDao
import com.example.kitabusf1.data.local.BookingWithBook
import com.example.kitabusf1.domain.Book
import com.example.kitabusf1.domain.Booking
import com.example.kitabusf1.domain.DAY_MS
import com.example.kitabusf1.domain.RENEW_DAYS
import com.example.kitabusf1.domain.isValidRentalDays
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//By default everyone is a student as there is no login
private const val DEFAULT_USER = "Student"

class LibraryRepository( //Passes the Dao's, the repo doesn't create them itself
    private val bookDao: BookDao,
    private val bookingDao: BookingDao
) {
//Uses domain as it's a rule about the apps data with no Android code in it
    fun getBooks(query: String = ""): Flow<List<Book>> =
        bookDao.searchBooks(query.trim().escapeLikeWildcards())
            .map { books -> books.map { it.toDomain() } }

    fun getCurrentBookings(): Flow<List<Booking>> =
        bookingDao.getCurrentBookings().map { bookings -> bookings.map { it.toDomain() } }

    //all functions are "suspend", this means called from a coroutine
    suspend fun reserveBook(bookId: Int, days: Int): Boolean {
        if (!isValidRentalDays(days)) return false //Guard that runs before anything touches the db

        val now = System.currentTimeMillis()
        return bookingDao.reserveBook(
            bookId = bookId,
            userName = DEFAULT_USER,
            bookingDate = now,
            returnDeadline = now + days * DAY_MS
        )
    }

    suspend fun collectBooking(bookingId: Int): Boolean =
        bookingDao.collectBooking(bookingId) > 0

    suspend fun renewBooking(bookingId: Int): Boolean =
        bookingDao.renewBooking(bookingId, RENEW_DAYS * DAY_MS) > 0

    suspend fun returnBooking(bookingId: Int): Boolean =
        bookingDao.returnBooking(bookingId)

    suspend fun cancelBooking(bookingId: Int): Boolean =
        bookingDao.cancelBooking(bookingId)
}
//toDomain is an extension function that adds a function to BookEntity from outside it
//These functions are kept private as turning entities into models is the repo's job
private fun BookEntity.toDomain() = Book(
    id = bookId,
    title = title,
    author = author,
    category = category,
    isAvailable = isAvailable
)

private fun BookingWithBook.toDomain() = Booking(
    id = booking.bookingId,
    book = book.toDomain(),
    userName = booking.userName,
    bookingDate = booking.bookingDate,
    returnDeadline = booking.returnDeadline,
    status = booking.status
)
//makes special characters ordinary
private fun String.escapeLikeWildcards(): String =
    replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_")
