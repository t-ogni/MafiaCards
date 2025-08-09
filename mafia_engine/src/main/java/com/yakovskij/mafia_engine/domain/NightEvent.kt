package com.yakovskij.mafia_engine.domain

import com.yakovskij.mafia_engine.domain.role.RoleType

sealed class NightEvent {
    data class KillAttempt(
        val performers: List<Player>,
        val performerGroup: RoleType,
        val target: Player,
        val wasSaved: Boolean = false
    ) : NightEvent()

    data class DoctorSaved(
        val performer: Player,
        val target: Player
    ) : NightEvent()

    data class DetectiveChecked(
        val performer: Player,
        val target: Player
    ) : NightEvent()

    data class SlutBlocked(
        val blocker: Player,
        val blocked: Player
    ) : NightEvent()

    data class ManiacKill(
        val performer: Player,
        val target: Player,
        val wasBlocked: Boolean
    ) : NightEvent()

    data class FailedAction(
        val performer: Player,
        val target: Player
    ) : NightEvent()

    data class Custom(
        val description: String
    ) : NightEvent()
}
