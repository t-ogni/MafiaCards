package com.yakovskij.mafiacards.features.localgame.data.gamesettings

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yakovskij.mafia_engine.domain.GameSettings
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafia_engine.domain.RoleType
import com.yakovskij.mafiacards.features.localgame.domain.TimerSettings
import com.yakovskij.mafiacards.gameSettingsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : IGameSettingsRepository{

    private val dataStore = context.gameSettingsDataStore

    private var gameSettings: GameSettings = GameSettings()
    private var timerSettings: TimerSettings = TimerSettings()

    private val _players = mutableStateOf<List<Player>>(emptyList())
    val players: State<List<Player>> get() = _players

    companion object {
        private val SETTINGS_KEY = stringPreferencesKey("game_settings_json")
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadSettings()
            syncPlayerList(gameSettings.getTotalRolesCount())
        }
    }

    suspend fun loadSettings() {
        val json = dataStore.data.first()[SETTINGS_KEY]
        if (!json.isNullOrBlank()) {
            gameSettings = parseJsonToGameSettings(json)
        }
    }

    private suspend fun saveSettings() {
        val json = convertGameSettingsToJson(gameSettings)
        dataStore.edit { prefs -> prefs[SETTINGS_KEY] = json }
    }

    suspend fun updateRoleCount(role: RoleType, count: Int) {
        gameSettings.setCount(role, count.coerceAtLeast(0))
        saveSettings()
        syncPlayerList(gameSettings.getTotalRolesCount())
    }

    suspend fun incrementRoleCount(role: RoleType) {
        updateRoleCount(role, gameSettings.getCountFor(role) + 1)
    }

    suspend fun decrementRoleCount(role: RoleType) {
        updateRoleCount(role, (gameSettings.getCountFor(role) - 1).coerceAtLeast(0))
    }

    fun getRoleCount(role: RoleType): Int = gameSettings.getCountFor(role)
    fun getTotalActiveRolesCount(): Int = gameSettings.getTotalActiveRolesCount()
    fun getTotalRolesCount(): Int = gameSettings.getTotalRolesCount()


    // --- Таймеры пока не сохраняем
    fun getDayTime() : Int { return timerSettings.dayTime }
    fun getNightTime() : Int { return timerSettings.nightTime }
    fun getVoteTime() : Int { return timerSettings.voteTime }

    // --- Таймеры пока не сохраняем
    fun updateDayTime(seconds: Int) { timerSettings = timerSettings.copy(dayTime = seconds) }
    fun updateNightTime(seconds: Int) { timerSettings = timerSettings.copy(nightTime = seconds) }
    fun updateVoteTime(seconds: Int) { timerSettings = timerSettings.copy(voteTime = seconds) }

    fun updatePlayerName(index: Int, name: String) {
        val updated = _players.value.toMutableList()
        if (index in updated.indices) {
            updated[index] = updated[index].copy(name = name)
            _players.value = updated
        }
    }

    private fun syncPlayerList(totalPlayers: Int) {
        val current = _players.value
        val newList = List(totalPlayers) { index ->
            current.getOrNull(index) ?: Player(index + 1, "Игрок ${index + 1}")
        }
        _players.value = newList
    }

    // --- JSON helpers

    private fun convertGameSettingsToJson(settings: GameSettings): String {
        val entries = settings.roleCounts.entries.joinToString(",") {
            "\"${it.key.name}\":${it.value}"
        }
        return "{$entries}"
    }

    private fun parseJsonToGameSettings(json: String): GameSettings {
        val regex = "\"(\\w+)\":(\\d+)".toRegex()
        val map = mutableMapOf<RoleType, Int>()

        for ((_, roleName, countStr) in regex.findAll(json).map { it.groupValues }) {
            RoleType.entries.find { it.name == roleName }?.let { role ->
                map[role] = countStr.toIntOrNull() ?: 0
            }
        }

        return GameSettings(roleCounts = map)
    }
}