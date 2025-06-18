package com.yakovskij.mafiacards.features.localgame.domain

data class TimerSettings(
    var dayTime: Int = 40,
    val voteTime: Int = 20,
    var nightTime: Int = 20
)