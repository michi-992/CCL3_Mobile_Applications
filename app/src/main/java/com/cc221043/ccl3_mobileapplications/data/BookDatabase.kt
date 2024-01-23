package com.cc221043.ccl3_mobileapplications.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cc221043.ccl3_mobileapplications.data.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = true)
@TypeConverters(com.cc221043.ccl3_mobileapplications.data.TypeConverters::class)
abstract class BookDatabase : RoomDatabase() {
    abstract val dao: BookDao
}