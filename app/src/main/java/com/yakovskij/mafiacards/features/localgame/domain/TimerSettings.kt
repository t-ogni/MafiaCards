package com.yakovskij.mafiacards.features.localgame.domain

data class TimerSettings(
    var dayTime: Int = 30,
    var nightTime: Int = 30,
    val voteTime: Int = 30
)