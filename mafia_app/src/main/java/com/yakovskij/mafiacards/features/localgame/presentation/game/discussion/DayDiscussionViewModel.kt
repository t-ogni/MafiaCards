package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class DayDiscussionViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: GameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(DayDiscussionUiState())
    val uiState: State<DayDiscussionUiState> = _uiState

    private var timerJob: Job? = null


    fun startDiscussion(){
        initDiscussionState()
        startTimer()
    }

    fun initDiscussionState() {
        if (!_uiState.value.shouldInit) return
        val dayTime = settingsRepository.getTimerSettings().dayTime
        val results = gameRepository.engine?.getNightResults() ?: emptyList()
        val formattedResults = formatNightSummary(results)

        _uiState.value = DayDiscussionUiState(
            nightResults = formattedResults,
            dayTimeSeconds = dayTime,
            dayTimeSecondsLeft = dayTime
        )
    }

    fun resetDiscussionState() {
        _uiState.value = DayDiscussionUiState()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.dayTimeSecondsLeft > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    dayTimeSecondsLeft = _uiState.value.dayTimeSecondsLeft - 1
                )
            }

            _uiState.value = _uiState.value.copy(isViewingEnded = true) // <- таймер завершён
        }
    }


    fun stopTimer() {
        timerJob?.cancel()
    }

    fun formatNightSummary(nightActions: List<NightAction>): List<String> {
        val result = mutableListOf<String>()

        // Группируем по целям: кто кого атаковал/лечил
        val mafiaActions = nightActions.filter { it.performer.role == RoleType.MAFIA }
        val doctorActions = nightActions.filter { it.performer.role == RoleType.DOCTOR }

        val killedMap = mutableMapOf<Int, MutableList<NightAction>>()
        mafiaActions.forEach {
            killedMap.getOrPut(it.target.id) { mutableListOf() }.add(it)
        }

        val savedSet = doctorActions.map { it.target.id }.toSet()

        val reportedTargets = mutableSetOf<Int>()

        // Обрабатываем по целям, чтобы понимать, спас ли доктор жертву мафии
        for ((targetId, actionsOnTarget) in killedMap) {
            val target = actionsOnTarget.first().target
            val mafiaRoles = actionsOnTarget.mapNotNull { it.performer.role?.title }.distinct()
            val mafiaStr = mafiaRoles.joinToString(", ")

            if (savedSet.contains(targetId)) {
                result += "Мафия ($mafiaStr) попыталась убить игрока ${target.name}, но Доктор спас его."
            } else {
                result += "Мафия ($mafiaStr) убила игрока ${target.name}."
            }

            reportedTargets.add(targetId)
        }

        // Обрабатываем оставшиеся действия: спасения без нападений, фейлы
        for (action in nightActions) {
            val performer = action.performer.role?.title ?: "???"
            val target = action.target.name

            if (action.performer.role == RoleType.DOCTOR && action.target.id !in reportedTargets) {
                result += "Доктор пытался спасти игрока $target, но его никто не трогал."
            }

            if (action.result == NightResult.FAILED && action.performer.role != RoleType.MAFIA && action.performer.role != RoleType.DOCTOR) {
                result += "$performer пытался воздействовать на $target, но это не сработало."
            }
        }

        return result
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Потенциальное утекание памяти
    }
}
