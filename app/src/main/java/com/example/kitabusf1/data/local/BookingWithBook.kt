package com.example.kitabusf1.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class BookingWithBook(
    @Embedded //puts book columns in
    val booking: BookingEntity,
    @Relation(parentColumn = "bookOwnerId", entityColumn = "bookId") //tells Room to get the book that matches the bookOwnerID
    val book: BookEntity
)