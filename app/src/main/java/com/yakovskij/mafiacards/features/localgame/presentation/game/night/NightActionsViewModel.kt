package com.yakovskij.mafiacards.features.localgame.presentation.game.night

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafiacards.features.game.data.*
import com.yakovskij.mafiacards.features.game.domain.*
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
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

    private val _uiState = mutableStateOf(NightUiState())
    val uiState: State<NightUiState> = _uiState


    fun startNight(){
        initNightState()
        nextUser()
    }

    fun initNightState() {
        if (!_uiState.value.shouldInit) return
        timerJob?.cancel()

        _uiState.value = _uiState.value.copy(
            nightIndex = 0,
            nightPlayersQueue = engine.getSession().state.players.filter { it.isAlive },
            shouldInit = false
        )
    }

    fun resetNightState() {
        _uiState.value = NightUiState()
    }


    fun nextUser() {
        if (_uiState.value.shouldInit) initNightState()

        timerJob?.cancel() // остановить предыдущий таймер, если есть
        if (_uiState.value.nightIndex >= _uiState.value.nightPlayersQueue.size) {
            _uiState.value = _uiState.value.copy(isNightFinished = true)
            engine.performNightActions()
            return
        }
        val index = _uiState.value.nightIndex
        val queue = _uiState.value.nightPlayersQueue
        val player = queue[index]
        val role = player.role

        val prompt = when (role) {
            RoleType.MAFIA -> "Вы мафия. Выберите игрока для устранения:"
            RoleType.DOCTOR -> "Вы доктор. Кого будете лечить?"
            RoleType.DETECTIVE -> "Вы детектив. Кого будете проверять?"
            RoleType.CIVILIAN, null -> "Вы мирный. Сделайте вид, что участвуете ночью.\nКто вам больше всего нравится?"
            else ->  "Вы — Непонятная мне роль."
        }

        val targets = gameRepository.getState()?.players?.filter { it.id != player.id && it.isAlive } ?: emptyList()

        _uiState.value = _uiState.value.copy(
            currentPlayer = player,
            role = role,
            promptText = prompt,
            availableTargets = targets,
            isActionTaken = false,
            timeLeftSeconds = settingsRepository.getTimerSettings().nightTime.toFloat()
        )
    }

    fun startNightTimer() {
        val nightTime = settingsRepository.getTimerSettings().nightTime
        _uiState.value = _uiState.value.copy(
            timeToAction = nightTime,
            timeLeftSeconds = nightTime.toFloat(),
            isTimeExpired = false // сбрасываем
        )

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeftSeconds > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    timeLeftSeconds = _uiState.value.timeLeftSeconds - 1f
                )
            }
            if (!_uiState.value.isActionTaken) {
                _uiState.value = _uiState.value.copy(isTimeExpired = true) // <- таймер завершён
                delay(500) // небольшая пауза для UI эффекта
                onNightActionSelected(null)
            }
        }
    }

    fun onNightActionSelected(target: Player?) {
        val player = _uiState.value.currentPlayer ?: return
        val role = _uiState.value.role ?: return

        if (target != null) {
            engine.user(player.id).targets(target.id)
        }

        _uiState.value = _uiState.value.copy(
            isActionTaken = true,
            isTimeExpired = false
        )

        timerJob?.cancel() // остановить таймер

        viewModelScope.launch {
            delay(500)
            _uiState.value = _uiState.value.copy(
                nightIndex = _uiState.value.nightIndex + 1
            )
            nextUser()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Потенциальное утекание памяти
    }
}
