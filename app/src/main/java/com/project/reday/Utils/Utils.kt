package com.project.reday.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Utils {
    fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            LocalDate.now().format(formatter)
        } else {
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            formatter.format(Date())
        }
    }
}