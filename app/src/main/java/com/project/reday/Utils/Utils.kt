package com.project.reday.Utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.project.reday.UI.home.adapter.Anniversary
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateToKorean(dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 E요일", Locale.KOREAN)

        val date = LocalDate.parse(dateString, inputFormatter)
        return date.format(outputFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateToShort(dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREAN)

        val date = LocalDate.parse(dateString, inputFormatter)
        return date.format(outputFormatter)
    }

    fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            LocalDate.now().format(formatter)
        } else {
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            formatter.format(Date())
        }
    }

    // 날짜 차이 계산
    fun calculateDaysBetween(startDate: String, endDate: String): Long {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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
    fun getDaysSinceFirstToBrokenDay(context: Context): Long? {
        val firstDate = DateManager(context).getDate("first_day")
        val brokenDate = DateManager(context).getDate("broken_day")
        return if (firstDate != null && brokenDate != null) calculateDaysBetween(firstDate, brokenDate) else null
    }

    // 다시 만난 날짜부터 현재까지 일수
    fun getDaysSinceReunion(context: Context): Long? {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val reunionDate = DateManager(context).getDate("re_day")
        val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
            dateFormatter.format(Date())
        }
        return reunionDate?.let { calculateDaysBetween(it, today) }
    }

    // 처음 만난 날짜부터 현재까지 일수
    fun getDaysSinceFristDay(context: Context): Long? {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val reunionDate = DateManager(context).getDate("first_day")
        val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
            dateFormatter.format(Date())
        }
        return reunionDate?.let { calculateDaysBetween(it, today) }
    }

    fun getReDays(context: Context): Long {
        val daysTogether = getDaysSinceFirstToBrokenDay(context) ?: 0
        val daysSinceReunion = getDaysSinceReunion(context) ?: 0
        return daysTogether + daysSinceReunion
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateUpcomingAnniversariesByTotalDay(
        startDate: String,
        totalDaysTogether: Long
    ): List<Anniversary> {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val start = LocalDate.parse(startDate, formatter)

        val totalDaysAnniversaries = mutableListOf<Anniversary>()

        // ✅ 총 만난 날 기준 기념일 계산
        for (i in 1..100) {
            val days = ((totalDaysTogether / 100) * 100) + (i * 100)
            val date = today.plusDays(days - totalDaysTogether)
            if (!date.isBefore(today)) {
                val daysLeft = ChronoUnit.DAYS.between(today, date).toInt()
                totalDaysAnniversaries.add(Anniversary("${days}일", date, daysLeft))
            }
        }

        for (year in 1..100) {
            val yearDate = start.plusYears(year.toLong())
            if (!yearDate.isBefore(today)) {
                val daysLeft = ChronoUnit.DAYS.between(today, yearDate).toInt()
                totalDaysAnniversaries.add(Anniversary("${year}주년", yearDate, daysLeft))
            }
        }

        // ✅ 날짜 기준으로 정렬 후 반환
        return totalDaysAnniversaries.sortedBy { it.daysLeft }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateUpcomingAnniversariesByReday(
        reDayDate: String,
        daysSinceReunion: Long
    ): List<Anniversary> {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val reDay = LocalDate.parse(reDayDate, formatter)

        val reDaysAnniversaries = mutableListOf<Anniversary>()

        // ✅ 다시 만난 날 기준 기념일 계산
        for (i in 1..100) {
            val days = ((daysSinceReunion / 100) * 100) + (i * 100)
            val date = today.plusDays(days - daysSinceReunion)
            if (!date.isBefore(today)) {
                val daysLeft = ChronoUnit.DAYS.between(today, date).toInt()
                reDaysAnniversaries.add(Anniversary("${days}일", date, daysLeft))
            }
        }

        for (year in 1..100) {
            val yearDate = reDay.plusYears(year.toLong())
            if (!yearDate.isBefore(today)) {
                val daysLeft = ChronoUnit.DAYS.between(today, yearDate).toInt()
                reDaysAnniversaries.add(Anniversary("${year}주년", yearDate, daysLeft))
            }
        }

        // ✅ 날짜 기준으로 정렬 후 반환
        return reDaysAnniversaries.sortedBy { it.daysLeft }
    }

}