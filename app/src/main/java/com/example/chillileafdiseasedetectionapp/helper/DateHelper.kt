package com.example.chillileafdiseasedetectionapp.helper

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateHelper {
    fun formatTimestamp(timestamp: Long): String {
        if (timestamp <= 0) {
            return "Waktu tidak valid"
        }

        val now = System.currentTimeMillis()
        val diff = now - timestamp

        if (diff < 60 * 1000) {
            return "Baru saja"
        }

        if (diff < 60 * 60 * 1000) {
            val minutes = (diff / (60 * 1000)).toInt()
            return "$minutes mnt lalu"
        }

        val calendarNow = Calendar.getInstance()
        val calendarTimestamp = Calendar.getInstance().apply { timeInMillis = timestamp }

        if (calendarNow.get(Calendar.YEAR) == calendarTimestamp.get(Calendar.YEAR)) {
            val dayDiff = calendarNow.get(Calendar.DAY_OF_YEAR) - calendarTimestamp.get(Calendar.DAY_OF_YEAR)
            if (dayDiff == 0) {
                val hours = (diff / (60 * 60 * 1000)).toInt()
                return "$hours jam lalu"
            } else if (dayDiff == 1) {
                return "Kemarin"
            }
        }

        return try {
            val sdf = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            "Tanggal tidak valid"
        }
    }
}