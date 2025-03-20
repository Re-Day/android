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

    // 날짜 저장 (String 형식으로 저장)
    fun saveDate(key: String, date: String) {
        sharedPreferences.edit().putString(key, date).apply()
    }

    // 날짜 불러오기 (String → Date 변환)
    fun getDate(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    // 날짜 차이 계산
    fun calculateDaysBetween(startDate: String, endDate: String): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ✅ Android 8.0 이상: LocalDate 사용
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val start = LocalDate.parse(startDate, formatter)
            val end = LocalDate.parse(endDate, formatter)
            ChronoUnit.DAYS.between(start, end) + 1
        } else {
            // ✅ Android 7.1 이하: Calendar 사용
            val start = dateFormatter.parse(startDate)
            val end = dateFormatter.parse(endDate)

            if (start != null && end != null) {
                val diff = end.time - start.time
                (diff / (1000 * 60 * 60 * 24)) + 1 // 밀리초 → 일 변환
            } else {
                0
            }
        }
    }

    // 처음 만난 날짜부터 헤어진 날짜까지 일수
    fun getDaysSinceFirstToBrokenDay(): Long? {
        val firstDate = getDate("first_day")
        val brokenDate = getDate("broken_day")
        return if (firstDate != null && brokenDate != null) calculateDaysBetween(firstDate, brokenDate) else null
    }

    // 다시 만난 날짜부터 현재까지 일수
    fun getDaysSinceReunion(): Long? {
        val reunionDate = getDate("re_day")
        val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
            dateFormatter.format(Date())
        }
        return reunionDate?.let { calculateDaysBetween(it, today) }
    }

    // 처음 만난 날짜부터 현재까지 일수
    fun getDaysSinceFristDay(): Long? {
        val reunionDate = getDate("first_day")
        val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
            dateFormatter.format(Date())
        }
        return reunionDate?.let { calculateDaysBetween(it, today) }
    }

    fun getReDays(): Long {
        val daysTogether = getDaysSinceFirstToBrokenDay() ?: 0
        val daysSinceReunion = getDaysSinceReunion() ?: 0
        return daysTogether + daysSinceReunion
    }
}
