package com.example.kitabusf1.data

import androidx.compose.runtime.mutableStateListOf
import com.example.kitabusf1.domain.Book
import com.example.kitabusf1.domain.Booking
import com.example.kitabusf1.domain.BookingStatus
import com.example.kitabusf1.domain.DAY_MS
import com.example.kitabusf1.domain.RENEW_DAYS

object PlaceholderData {

    private val now = System.currentTimeMillis()

    val books = mutableStateListOf(
        Book(1, "Long Walk to Freedom", "Nelson Mandela", "Biography"),
        Book(2, "Born a Crime", "Trevor Noah", "Memoir", isAvailable = false),
        Book(3, "Cry, the Beloved Country", "Alan Paton", "Classic"),
        Book(4, "Disgrace", "J.M. Coetzee", "Fiction", isAvailable = false),
        Book(5, "The Promise", "Damon Galgut", "Fiction", isAvailable = false),
        Book(6, "Things Fall Apart", "Chinua Achebe", "Classic"),
        Book(7, "Nervous Conditions", "Tsitsi Dangarembga", "Fiction"),
        Book(8, "Welcome to Our Hillbrow", "Phaswane Mpe", "Fiction", isAvailable = false),
        Book(9, "July's People", "Nadine Gordimer", "Fiction"),
        Book(10, "Half of a Yellow Sun", "Chimamanda Ngozi Adichie", "Historical")
    )

    private fun book(id: Int) = books.first { it.id == id }

    val bookings = mutableStateListOf(
        Booking(1, book(2), "Student", now - 12 * DAY_MS, now + 2 * DAY_MS, BookingStatus.ACTIVE),
        Booking(2, book(4), "Student", now - 15 * DAY_MS, now - 1 * DAY_MS, BookingStatus.ACTIVE),
        Booking(3, book(5), "Student", now - 9 * DAY_MS, now + 5 * DAY_MS, BookingStatus.ACTIVE),
        Booking(4, book(8), "Student", now, now + 14 * DAY_MS, BookingStatus.PENDING)
    )

    fun reserveBook(book: Book, days :Int ){
        val index = books.indexOfFirst{ it.id == book.id } //finds where the book is in the list
        if (index == -1 || !books[index].isAvailable) return

        val reserved = books[index].copy(isAvailable = false) //safety check: Does nothing if book isn't found or is reserved
        books[index] = reserved

        val start = System.currentTimeMillis()
        bookings.add( //creates the booking
            Booking(
                id = (bookings.maxOfOrNull { it.id } ?: 0 ) + 1,
                book = reserved,
                userName = "Student",
                bookingDate = start,
                returnDeadline = start + days * DAY_MS,
                status = BookingStatus.PENDING
            )
        )
    }

    //Works like reserve booking: Finds book position, safety checks it for Active book then when passed, makes the renewal and puts it back on screen to be viewed
    fun renewBooking(booking: Booking): Booking? {
        val index = bookings.indexOfFirst { it.id == booking.id }
        if (index == -1 || bookings[index].status != BookingStatus.ACTIVE) return null

        val renewed = bookings[index].copy (
            returnDeadline = bookings[index].returnDeadline + RENEW_DAYS * DAY_MS
        )
        bookings[index] = renewed
        return renewed
    }

    fun returnBooking(booking: Booking): Boolean {
        val index = bookings.indexOfFirst { it.id == booking.id }
        if (index == -1 || bookings[index].status != BookingStatus.ACTIVE) return false

        bookings[index] = bookings[index].copy(status = BookingStatus.RETURNED)
        makeAvailable(booking.book.id)
        return true
    }
    //Only works on Pending books
    fun cancelBooking(booking: Booking): Boolean {
        val index = bookings.indexOfFirst { it.id == booking.id }
        if (index == -1 || bookings[index].status != BookingStatus.PENDING) return false

        bookings.removeAt(index)
        makeAvailable(booking.book.id)
        return true
    }
    //Function for collecting the book
    fun collectBooking(booking: Booking): Boolean {
        val index = bookings.indexOfFirst { it.id == booking.id }
        if (index == -1 || bookings[index].status != BookingStatus.PENDING) return false

        bookings[index] = bookings[index].copy(status = BookingStatus.ACTIVE)
        return true
    }

    //frees the book from any occupied status
    private fun makeAvailable(bookId: Int) {
        val index = books.indexOfFirst { it.id == bookId }
        if (index != -1) books[index] = books[index].copy(isAvailable = true)
    }

}
