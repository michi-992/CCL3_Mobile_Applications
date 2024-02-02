package com.cc221043.ccl3_mobileapplications.data

import androidx.room.TypeConverter

// converts Lists of Strings to a single String and vice versa
class TypeConverters {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(separator = "•") { it }
    }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        return value?.split("•")?.map { it.trim() }
    }
}