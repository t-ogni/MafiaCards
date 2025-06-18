package com.yakovskij.mafia_engine.domain

sealed class NightEvent {
    data class KillAttempt(
        val performers: List<Player>,
        val target: Player,
        val wasSaved: Boolean
    ) : NightEvent()

    data class Saved(
        val performer: Player,
        val target: Player
    ) : NightEvent()

    data class FailedAction(
        val performer: Player,
        val target: Player,
        val roleType: RoleType
    ) : NightEvent()

    data class Blocked(
        val blocker: Player,
        val blocked: Player
    ) : NightEvent()

    data class ManiacKill(
        val performer: Player,
        val target: Player,
        val wasBlocked: Boolean
    ) : NightEvent()

    data class Custom(val description: String) : NightEvent()
}
