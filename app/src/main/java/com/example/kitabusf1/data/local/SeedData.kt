package com.example.kitabusf1.data.local

import com.example.kitabusf1.domain.BookingStatus

//Seed Data: Holds starting Data
private const val DAY_MS = 24 * 60 * 60 * 1000L

object SeedData {

    val books = listOf(
        BookEntity(1, "The Role of a Great Game Designer", "Richard Carrillo", "Game Design"),
        BookEntity(2, "Narrative Design for Indies: Getting Started", "Edwin McRae", "Game Design", isAvailable = false),
        BookEntity(3, "Game Anim: Video Game Animation Explained", "Jonathan Cooper", "3D Animation"),
        BookEntity(4, "Technical Animation in Video Games", "Matthew Lake", "3D Animation", isAvailable = false),
        BookEntity(5, "Race Car Vehicle Dynamics", "William F. Milliken & Douglas L. Milliken", "Motorsport Engineering", isAvailable = false),
        BookEntity(6, "Competition Car Aerodynamics", "Simon McBeath", "Motorsport Engineering"),
        BookEntity(7, "Cybersecurity 101: A Dummy's Guide for Beginners", "L.D. Knowings", "Cyber Security"),
        BookEntity(8, "Cybersecurity for Beginners", "Raef Meeuwisse", "Cyber Security", isAvailable = false),
        BookEntity(9, "Software Engineering", "University of Cape Town", "Software Engineering"),
        BookEntity(10, "Software Engineering", "Tutorialspoint", "Software Engineering")
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