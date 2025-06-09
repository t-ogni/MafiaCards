package com.yakovskij.mafiacards.features.game.data

import com.yakovskij.mafiacards.features.game.domain.GameException
import com.yakovskij.mafiacards.features.game.domain.NightAction
import com.yakovskij.mafiacards.features.game.domain.NightResult
import com.yakovskij.mafiacards.features.game.domain.RoleType

class NightProcessor(private var session: GameSession) {
    private val actions = mutableListOf<NightAction>()

    private var lastNightResults: List<NightAction> = emptyList()
    fun getLastNightResults(): List<NightAction> = lastNightResults

    fun addAction(performerId: Int, targetId: Int, roleType: RoleType) {
        val performer = session.state.players.firstOrNull { it.id == performerId }!!

        if (!performer.isAlive) throw GameException.DeadPlayerAction()

        val target = session.state.players.firstOrNull { it.id == targetId }
            ?: throw GameException.InvalidTarget()

        if (!target.isAlive) throw GameException.TargetIsDead()

        actions.removeAll { it.performerId == performerId && it.roleType == roleType }
        actions.add(NightAction(performerId, targetId, roleType))
    }

    fun getActions(): List<NightAction> = actions.toList()
    fun clearUserActions(performerId: Int): Boolean = actions.removeAll { it.performerId == performerId }

    fun clearActions() {
        actions.clear()
    }

    fun setSession(newSession: GameSession) {
        session = newSession
    }

    fun performNightActions() {
        val mafiaTargets = actions.filter { it.roleType == RoleType.MAFIA }.map { it.targetId }
        val doctorTarget = actions.findLast { it.roleType == RoleType.DOCTOR }?.targetId

        val mafiaTarget = mafiaTargets.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

        val results = actions.map { action ->
            when (action.roleType) {
                RoleType.MAFIA -> {
                    if (action.targetId == mafiaTarget && action.targetId != doctorTarget) {
                        session.eliminatePlayer(action.targetId)
                        action.copy(result = NightResult.KILLED)
                    } else {
                        action.copy(result = NightResult.FAILED)
                    }
                }
                RoleType.DOCTOR -> {
                    if (action.targetId == mafiaTarget) {
                        action.copy(result = NightResult.SAVED)
                    } else {
                        action.copy(result = NightResult.FAILED)
                    }
                }
                else -> action.copy(result = NightResult.NONE)
            }
        }

        lastNightResults = results
        clearActions()
    }

}
