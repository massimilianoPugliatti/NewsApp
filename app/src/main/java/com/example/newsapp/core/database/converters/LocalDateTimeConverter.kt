package com.example.newsapp.core.database.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it.replace("Z", "")) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? = date?.toString()

}