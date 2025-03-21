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

    // 타입 저장 (2: first_day / 1: re_day)
    fun saveType(type: Int) {
        sharedPreferences.edit().putInt("type", type).apply()
    }

    // 타입 불러오기
    fun getType(): Int {
        return sharedPreferences.getInt("type", 0)
    }

    // 날짜 저장 (String 형식으로 저장)
    fun saveDate(key: String, date: String) {
        sharedPreferences.edit().putString(key, date).apply()
    }

    // 날짜 불러오기 (String → Date 변환)
    fun getDate(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}
