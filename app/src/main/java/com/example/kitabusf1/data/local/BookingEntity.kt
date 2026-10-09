package com.example.kitabusf1.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.kitabusf1.data.BookingStatus

@Entity(
    tableName = "bookings",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class, //Table being pointed at
            parentColumns = ["bookId"], //The column in books
            childColumns = ["bookOwnerId"], //Column in bookings
            onDelete = ForeignKey.CASCADE //When a book is deleted, all its bookings go with it
        )
    ],
    indices = [Index("bookOwnerId")] //Helps database find all a books bookings without reading all rows
)
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val bookingId: Int = 0,
    val bookOwnerId: Int,
    val userName: String,
    val bookingDate: Long, //Milliseconds
    val returnDeadline: Long, //Milliseconds
    val status: BookingStatus
)