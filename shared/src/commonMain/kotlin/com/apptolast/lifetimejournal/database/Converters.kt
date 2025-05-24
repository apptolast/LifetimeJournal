package com.apptolast.lifetimejournal.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class Converters {

    // LocalDate converters
//    @TypeConverter
//    fun fromLocalDate(date: LocalDate): String {
//        return date.toString()
//    }
//
//    @TypeConverter
//    fun toLocalDate(dateString: String): LocalDate {
//        return LocalDate.parse(dateString)
//    }

    // List<Int> converters (for entry IDs in journal)
//    @TypeConverter
//    fun fromIntList(list: List<Int>): String {
//        return Json.encodeToString(list)
//    }
//
//    @TypeConverter
//    fun toIntList(string: String): List<Int> {
//        return if (string.isBlank()) {
//            emptyList()
//        } else {
//            Json.decodeFromString(string)
//        }
//    }

    @TypeConverter
    fun fromTimestamp(value: Int?): LocalDate? {
        return value?.let { LocalDate.fromEpochDays(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Int? {
        return date?.toEpochDays()
    }
}

