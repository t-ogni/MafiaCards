package com.yakovskij.mafiacards.features.localgame.presentation.game.night

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafiacards.features.game.data.*
import com.yakovskij.mafiacards.features.game.domain.*
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.presentation.game.GameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NightActionsViewModel @Inject constructor(
    private var gameRepository: GameRepository,
    private var settingsRepository: GameSettingsRepository,
) : ViewModel() {

    private var engine: GameEngine = gameRepository.engine!!

    private var timerJob: Job? = null

    private var nightPlayersQueue: List<Player> = emptyList()
    private var nightIndex = 0

    private val _uiState = mutableStateOf(NightUiState())
    val uiState: State<NightUiState> = _uiState

    fun initNight() {
        if (!_uiState.value.shouldInit) return

        timerJob?.cancel()
        nightIndex = 0
        nightPlayersQueue = engine.getSession().state.players.filter { it.isAlive }

        _uiState.value = NightUiState(shouldInit = false)
        nextNightTurn()
    }


    private fun nextNightTurn() {
        timerJob?.cancel() // остановить предыдущий таймер, если есть

        if (nightIndex >= nightPlayersQueue.size) {
            _uiState.value = _uiState.value.copy(isNightFinished = true)
            return
        }

        val player = nightPlayersQueue[nightIndex]
        val role = player.role?.type

        val prompt = when (role) {
            RoleType.MAFIA -> "Вы мафия. Выберите игрока для устранения:"
            RoleType.DOCTOR -> "Вы доктор. Кого будете лечить?"
            RoleType.DETECTIVE -> "Вы детектив. Кого будете проверять?"
            RoleType.CIVILIAN, null -> "Вы мирный. Сделайте вид, что участвуете ночью.\nКто вам больше всего нравится?"
            else ->  "Вы — Непонятная мне роль."
        }

        val targets = gameRepository.getState()?.players?.filter { it.id != player.id && it.isAlive } ?: emptyList()

        _uiState.value = NightUiState(
            currentPlayer = player,
            role = role,
            promptText = prompt,
            availableTargets = targets,
            isActionTaken = false,
            timeLeftSeconds = settingsRepository.getTimerSettings().nightTime.toFloat()
        )

        startNightTimer()
    }

    private fun startNightTimer() {
        _uiState.value = _uiState.value.copy(timeToAction = settingsRepository.getTimerSettings().nightTime)

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeftSeconds > 0) {
                delay(100)
                _uiState.value = _uiState.value.copy(
                    timeLeftSeconds = _uiState.value.timeLeftSeconds - 0.1f
                )
            }
            if (!_uiState.value.isActionTaken) {
                onNightActionSelected(null) // игрок не сделал выбор — пропускаем
            }
        }
    }

    fun onNightActionSelected(target: Player?) {
        val player = _uiState.value.currentPlayer ?: return
        val role = _uiState.value.role ?: return

        if (target != null) {
            engine.user(player.id).targets(target.id, role)
        }

        _uiState.value = _uiState.value.copy(isActionTaken = true)
        timerJob?.cancel() // остановить таймер

        viewModelScope.launch {
            nightIndex++
            nextNightTurn()
        }
    }
}
