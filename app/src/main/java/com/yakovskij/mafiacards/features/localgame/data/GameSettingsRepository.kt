package com.yakovskij.mafiacards.features.localgame.data

import com.yakovskij.mafiacards.features.game.domain.GameSettings
import com.yakovskij.mafiacards.features.localgame.domain.TimerSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameSettingsRepository @Inject constructor() {

    private var gameSettings: GameSettings = GameSettings()
    private var timerSettings: TimerSettings = TimerSettings()

    fun getGameSettings(): GameSettings = gameSettings.copy()
    fun getTimerSettings(): TimerSettings = timerSettings.copy()

    fun updateTotalPlayers(count: Int) {
        gameSettings = gameSettings.copy(totalPlayers = count)
    }

    fun updateMafiaCount(count: Int) {
        gameSettings = gameSettings.copy(mafiaCount = count.coerceAtLeast(0))
    }

    fun updateDoctorCount(count: Int) {
        gameSettings = gameSettings.copy(doctorCount = count.coerceAtLeast(0))
    }

    fun updateDetectiveCount(count: Int) {
        gameSettings = gameSettings.copy(detectiveCount = count.coerceAtLeast(0))
    }

    fun getMafiaCount() = gameSettings.mafiaCount
    fun getDoctorCount() = gameSettings.doctorCount
    fun getDetectiveCount() = gameSettings.detectiveCount

    fun updateDayTime(seconds: Int) {
        timerSettings = timerSettings.copy(dayTime = seconds)
    }

    fun updateNightTime(seconds: Int) {
        timerSettings = timerSettings.copy(nightTime = seconds)
    }

    fun updateVoteTime(seconds: Int) {
        timerSettings = timerSettings.copy(voteTime = seconds)
    }

}