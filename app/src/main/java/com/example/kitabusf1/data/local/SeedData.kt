package com.example.kitabusf1.data.local

import com.example.kitabusf1.data.BookingStatus

//Seed Data: Holds starting Data
private const val DAY_MS = 24 * 60 * 60 * 1000L

object SeedData {

    val books = listOf(
        BookEntity(1, "Long Walk to Freedom", "Nelson Mandela", "Biography"),
        BookEntity(2, "Born a Crime", "Trevor Noah", "Memoir", isAvailable = false),
        BookEntity(3, "Cry, the Beloved Country", "Alan Paton", "Classic"),
        BookEntity(4, "Disgrace", "J.M. Coetzee", "Fiction", isAvailable = false),
        BookEntity(5, "The Promise", "Damon Galgut", "Fiction", isAvailable = false),
        BookEntity(6, "Things Fall Apart", "Chinua Achebe", "Classic"),
        BookEntity(7, "Nervous Conditions", "Tsitsi Dangarembga", "Fiction"),
        BookEntity(8, "Welcome to Our Hillbrow", "Phaswane Mpe", "Fiction", isAvailable = false),
        BookEntity(9, "July's People", "Nadine Gordimer", "Fiction"),
        BookEntity(10, "Half of a Yellow Sun", "Chimamanda Ngozi Adichie", "Historical")
    )


    fun bookings(now: Long = System.currentTimeMillis()) = listOf(
        booking(bookId = 2, from = now - 12 * DAY_MS, until = now + 2 * DAY_MS, BookingStatus.ACTIVE),
        booking(bookId = 4, from = now - 15 * DAY_MS, until = now - 1 * DAY_MS, BookingStatus.ACTIVE),
        booking(bookId = 5, from = now - 9 * DAY_MS, until = now + 5 * DAY_MS, BookingStatus.ACTIVE),
        booking(bookId = 8, from = now, until = now + 14 * DAY_MS, BookingStatus.PENDING)
    )

    //Acts as a helper to not have field names repeating
    private fun booking(bookId: Int, from: Long, until: Long, status: BookingStatus) =
        BookingEntity(
            bookOwnerId = bookId,
            userName = "Student",
            bookingDate = from,
            returnDeadline = until,
            status = status
        )

}