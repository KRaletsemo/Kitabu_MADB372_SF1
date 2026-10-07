package com.example.kitabusf1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books") //Creating a table and naming it books
data class BookEntity(
    @PrimaryKey(autoGenerate = true) //Primary key will be created automatically and kept track of
    val bookId: Int = 0,
    val title: String,
    val author: String,
    val category: String,
    val isAvailable: Boolean = true
)