package com.example.todo.core.Utils

import androidx.room.TypeConverter
import com.example.todo.data.local.Step
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {

    // Convert List<String> <-> String
    @TypeConverter
    fun fromList(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromStepList(value: List<Step>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStepList(value: String): List<Step> {
        val listType = object : TypeToken<List<Step>>() {}.type
        return Gson().fromJson(value, listType)
    }

}

// Convert Long? <-> Date String (if needed)
//@TypeConverter
//fun fromTimestamp(value: Long?): Date? {
//    return value?.let { Date(it) }
//}
//
//@TypeConverter
//fun dateToTimestamp(date: Date?): Long? {
//    return date?.time
//}