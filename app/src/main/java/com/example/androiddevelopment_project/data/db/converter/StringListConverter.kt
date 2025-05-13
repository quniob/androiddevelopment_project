package com.example.androiddevelopment_project.data.db.converter

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(separator = "|")
    }
    
    @TypeConverter
    fun toList(data: String): List<String> {
        return if (data.isEmpty()) {
            emptyList()
        } else {
            data.split("|")
        }
    }
} 