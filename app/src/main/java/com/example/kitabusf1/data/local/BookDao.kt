package com.example.kitabusf1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: BookEntity): Long //Function to insert book and returns an ID of type Long

    @Insert
    suspend fun insertBooks(books: List<BookEntity>) // Will add the whole list of books

    @Query("SELECT * FROM books ORDER BY bookId")
    fun getAllBooks(): Flow<List<BookEntity>> // Function to get all books

    @Query("SELECT * FROM books WHERE isAvailable = :isAvailable ORDER BY bookId")
    fun getBooksByAvailability(isAvailable: Boolean): Flow<List<BookEntity>> // Function to get all books by availability

    @Query(
        "SELECT * FROM books " +
                "WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' " +
                "ORDER BY bookId"
    )
    fun searchBooks(query: String): Flow<List<BookEntity>> //Function to search books
}