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
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Update
    suspend fun updateBook(partner: Book)

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>
}