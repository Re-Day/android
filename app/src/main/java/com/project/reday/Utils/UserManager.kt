package com.project.reday.Utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale

class UserManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // 닉네임 저장
    fun saveNickname(name: String) {
        sharedPreferences.edit().putString("nickname", name).apply()
    }

    // 닉네임 불러오기
    fun getNickname(): String? {
        return sharedPreferences.getString("nickname", null)
    }
}