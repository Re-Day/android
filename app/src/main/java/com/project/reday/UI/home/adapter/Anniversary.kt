package com.project.reday.UI.home.adapter

import java.time.LocalDate

data class Anniversary(
    val title: String,
    val date: LocalDate,
    val daysLeft: Int
)