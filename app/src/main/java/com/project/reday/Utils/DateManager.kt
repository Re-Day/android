package com.project.reday.Utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("date_prefs", Context.MODE_PRIVATE)
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // 타입 저장 (2: first_day / 1: re_day)
    fun saveType(type: Int) {
        sharedPreferences.edit().putInt("type", type).apply()
    }

    // 타입 불러오기
    fun getType(): Int {
        return sharedPreferences.getInt("type", 0)
    }
}
