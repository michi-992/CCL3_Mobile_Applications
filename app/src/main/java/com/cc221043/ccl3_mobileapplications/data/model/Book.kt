package com.cc221043.ccl3_mobileapplications.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @ColumnInfo(name = "title")
    val title: String,
    val author: String = "",
    var cover: String = "",
    val status: String,
    val genre: List<String> = emptyList(),
    val platformat: String,
    val synopsis: String,
    val rating: Int?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)