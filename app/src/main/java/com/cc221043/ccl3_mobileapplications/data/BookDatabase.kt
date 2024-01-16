package com.cc221043.ccl3_mobileapplications.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cc221043.ccl3_mobileapplications.data.model.Book

@Database(entities = [Book::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract val dao: BookDao
}