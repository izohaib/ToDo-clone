package com.example.todo.core.Utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    fun convertLongToDate(timeInMillis: Long): String {
        val date = Date(timeInMillis)
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun convertLongToTime(timeInMillis: Long): String {
        val time = Date(timeInMillis)
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault()) // For 12-hour format
        // val format = SimpleDateFormat("HH:mm", Locale.getDefault()) // Use this for 24-hour format
        return format.format(time)
    }
    fun combineDateTime(dateMillis: Long, timeMillis: Long): Long {
        val dateCal = Calendar.getInstance().apply { timeInMillis = dateMillis }
        val timeCal = Calendar.getInstance().apply { timeInMillis = timeMillis }

        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
        dateCal.set(Calendar.SECOND, 0)
        dateCal.set(Calendar.MILLISECOND, 0)

        return dateCal.timeInMillis
    }

}