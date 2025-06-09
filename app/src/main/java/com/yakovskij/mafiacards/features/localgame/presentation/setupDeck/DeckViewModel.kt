package com.yakovskij.mafiacards.features.localgame.presentation.setupDeck

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafiacards.features.game.domain.RoleType
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class DeckViewModel @Inject constructor(
    private val repository: GameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(
        DeckUiState(
            mafiaCount = repository.getMafiaCount(),
            doctorCount = repository.getDoctorCount(),
            detectiveCount = repository.getDetectiveCount(),
            totalPlayers = repository.getGameSettings().totalPlayers
        )
    )
    val uiState: State<DeckUiState> = _uiState

    init {
        validateInitialState()
    }

    private fun validateInitialState() {
        val current = _uiState.value
        val civilians = current.totalPlayers - current.specialRolesCount

        _uiState.value = current.copy(
            errorMessage = if (civilians < 0) "Слишком много активных ролей" else null
        )
    }

    fun onRoleCountChange(role: RoleType, newCount: Int) {
        val current = _uiState.value
        val safeCount = newCount.coerceAtLeast(0)

        val newState = when (role) {
            RoleType.MAFIA -> current.copy(mafiaCount = safeCount)
            RoleType.DOCTOR -> current.copy(doctorCount = safeCount)
            RoleType.DETECTIVE -> current.copy(detectiveCount = safeCount)
            else -> current
        }

        val totalRoles = newState.specialRolesCount
        val civilians = newState.totalPlayers - totalRoles

        when {
            civilians < 0 -> {
                _uiState.value = newState.copy(errorMessage = "Слишком много активных ролей")
            }

            civilians == 0 && safeCount > (when (role) {
                RoleType.MAFIA -> current.mafiaCount
                RoleType.DOCTOR -> current.doctorCount
                RoleType.DETECTIVE -> current.detectiveCount
                else -> 0
            }) -> {
                // блокируем увеличение, но не показываем ошибку
                return
            }

            else -> {
                _uiState.value = newState.copy(errorMessage = null)
                // сохраняем в репозиторий
                when (role) {
                    RoleType.MAFIA -> repository.updateMafiaCount(safeCount)
                    RoleType.DOCTOR -> repository.updateDoctorCount(safeCount)
                    RoleType.DETECTIVE -> repository.updateDetectiveCount(safeCount)
                    else -> Unit
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

}

