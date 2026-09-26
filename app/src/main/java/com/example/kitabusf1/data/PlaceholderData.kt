package com.example.kitabusf1.data

import kotlin.math.ceil

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val isAvailable: Boolean = true
)

enum class BookingStatus { PENDING, ACTIVE, RETURNED }

data class Booking(
    val id: Int,
    val book: Book,
    val userName: String,
    val bookingDate: Long,
    val returnDeadline: Long,
    val status: BookingStatus
)

private const val DAY_MS = 24 * 60 * 60 * 1000L

fun Book.matchesSearch(query: String): Boolean {
    val q = query.trim()
    return q.isEmpty() ||
        title.contains(q, ignoreCase = true) ||
        author.contains(q, ignoreCase = true)
}

fun Booking.isOverdue(now: Long = System.currentTimeMillis()): Boolean = returnDeadline < now

fun Booking.daysRemaining(now: Long = System.currentTimeMillis()): Int =
    ceil((returnDeadline - now).toDouble() / DAY_MS).toInt()

object PlaceholderData {

    private val now = System.currentTimeMillis()

    val books = listOf(
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

    val bookings = listOf(
        Booking(1, book(2), "Student", now - 12 * DAY_MS, now + 2 * DAY_MS, BookingStatus.ACTIVE),
        Booking(2, book(4), "Student", now - 15 * DAY_MS, now - 1 * DAY_MS, BookingStatus.ACTIVE),
        Booking(3, book(5), "Student", now - 9 * DAY_MS, now + 5 * DAY_MS, BookingStatus.ACTIVE),
        Booking(4, book(8), "Student", now, now + 14 * DAY_MS, BookingStatus.PENDING)
    )
}
