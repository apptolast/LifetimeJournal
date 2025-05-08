package com.apptolast.lifetimejournal.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

class Converters {

    // LocalDate converters
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }

    // List<Int> converters (for entry IDs in journal)
    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toIntList(string: String): List<Int> {
        return if (string.isBlank()) {
            emptyList()
        } else {
            Json.decodeFromString(string)
        }
    }
}
