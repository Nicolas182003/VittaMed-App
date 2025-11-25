package com.example.vittamed.model

data class Schedule(
    val morning: ArrayList<HourInterval>,
    val afternoon: ArrayList<HourInterval>
)
