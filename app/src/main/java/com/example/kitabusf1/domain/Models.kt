package com.example.kitabusf1.domain

import kotlin.math.ceil

const val DAY_MS = 24 * 60 * 60 * 1000L
const val RENEW_DAYS = 7 //renewal days is 7

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

fun Book.matchesSearch(query: String): Boolean {
    val q = query.trim()
    return q.isEmpty() ||
        title.contains(q, ignoreCase = true) ||
        author.contains(q, ignoreCase = true)
}

fun Booking.isOverdue(now: Long = System.currentTimeMillis()): Boolean = returnDeadline < now

fun Booking.daysRemaining(now: Long = System.currentTimeMillis()): Int =
    ceil((returnDeadline - now).toDouble() / DAY_MS).toInt()
