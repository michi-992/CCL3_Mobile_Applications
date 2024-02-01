package com.cc221043.ccl3_mobileapplications.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cc221043.ccl3_mobileapplications.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert
    suspend fun insertBook(book: Book): Long

    @Delete
    suspend fun deleteBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBookById(id: Int): Flow<Book>

    @Query("SELECT * FROM books WHERE LOWER(title) LIKE LOWER('%' || :searchText || '%')")
    fun getBooksBySearch(searchText: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE genres IN (:genreQuery)")
    fun getBooksByGenres(genreQuery: List<String>): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE status = :status")
    fun getBooksByStatus(status: String): Flow<List<Book>>
}