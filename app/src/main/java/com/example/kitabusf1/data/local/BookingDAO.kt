package com.example.kitabusf1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.kitabusf1.data.Booking
import com.example.kitabusf1.data.BookingStatus
import kotlinx.coroutines.flow.Flow

//DAO - Data Access Object
@Dao
interface BookingDAO {

    @Insert
    suspend fun insertBooking(booking: BookingEntity): Long

    @Insert
    suspend fun  insertBookings(bookings: List<BookingEntity>)

    @Transaction
    @Query("SELECT * FROM bookings WHERE status != 'RETURNED' ORDER BY returnDeadline")
    fun getCurrentBookings(): Flow<List<BookingWithBook>> //get all books and ordered by deadline

    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId")
    suspend fun getBooking(bookingId: Int): BookingEntity?

    @Query(
        "UPDATE bookings SET returnDeadline = returnDeadline + :extraMillis " +
                "WHERE bookingId = :bookingId AND status = 'ACTIVE'"
    )

    suspend fun renewBooking(bookingId: Int, extraMillis: Long): Int //Function to add extra time to a book

    @Query("UPDATE bookings SET status = 'ACTIVE' WHERE bookingId = :bookingId AND status = 'PENDING'")
    suspend fun collectBooking(bookingId: Int): Int //changes the status of the book from Pending to Active

    @Query("UPDATE bookings SET status = :status WHERE bookingId = :bookingId")
    suspend fun updateStatus(bookingId: Int, status: BookingStatus)

    @Query("DELETE FROM bookings WHERE bookingId = :bookingId AND status = 'PENDING'")
    suspend fun deletePendingBooking(bookingId: Int): Int //deletes book thats still pending

    @Query("SELECT isAvailable FROM books WHERE bookId = :bookId")
    suspend fun isBookAvailable(bookId: Int): Boolean?

    @Query("UPDATE books SET isAvailable = :isAvailable WHERE bookId = :bookId")
    suspend fun setBookAvailability(bookId: Int, isAvailable: Boolean)

    @Transaction
    suspend fun reserveBook(
        bookId: Int,
        userName: String,
        bookingDate: Long,
        returnDeadline: Long
    ): Boolean {
        if (isBookAvailable(bookId) != true) return false

        setBookAvailability(bookId, false)
        insertBooking(
            BookingEntity(
                bookOwnerId = bookId,
                userName = userName,
                bookingDate = bookingDate,
                returnDeadline = returnDeadline,
                status = BookingStatus.PENDING
            )
        )
        return true
    }

    @Transaction
    suspend fun returnBooking(bookingId: Int): Boolean {
        val booking = getBooking(bookingId) ?: return false
        if (booking.status != BookingStatus.ACTIVE) return false

        updateStatus(bookingId, BookingStatus.RETURNED)
        setBookAvailability(booking.bookOwnerId, true)
        return true
    }

    @Transaction
    suspend fun cancelBooking(bookingId: Int): Boolean {
        val booking = getBooking(bookingId) ?: return false
        if (deletePendingBooking(bookingId) == 0) return false

        setBookAvailability(booking.bookOwnerId, true)
        return true
    }
}