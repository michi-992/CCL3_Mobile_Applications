package com.cc221043.ccl3_mobileapplications.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    val title: String,
    val author: String = "",
    val cover: String = "",
    val status: String,
    val genre: List<String> = emptyList(),
    val platformat: String,
    val synopsis: String,
    val rating: Int?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)