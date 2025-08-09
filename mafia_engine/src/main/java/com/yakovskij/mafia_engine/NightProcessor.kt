package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.domain.role.RoleType
import kotlin.collections.mutableListOf

class NightProcessor(private var session: GameSession) {
    private val actions = mutableListOf<NightAction>()
    private val events = mutableListOf<NightEvent>()

    fun addAction(performerId: Int, targetId: Int) {
        val performer = session.getPlayerById(performerId) ?: throw GameException.PlayerNotFound()

        if (!performer.isAlive) throw GameException.DeadPlayerAction()

        val target = session.state.players.firstOrNull { it.id == targetId }
            ?: throw GameException.InvalidTarget()

        if (!target.isAlive) throw GameException.TargetIsDead()

        actions.removeAll { it.performer.id == performer.id && it.performer.role == performer.role }
        actions.add(NightAction(performer, target))
    }

    fun getActions(): List<NightAction> = actions.toList()
    fun getEvents(): List<NightEvent> = events.toList()

    fun clearUserActions(performerId: Int): Boolean = actions.removeAll { it.performer.id == performerId }

    fun clearActions() {
        actions.clear()
    }
    fun clearEvents() {
        events.clear()
    }

    fun setSession(newSession: GameSession) {
        session = newSession
    }

    fun performNightActions(): List<NightEvent> {
        val players = session.state.players

        val blockActions = actions.filter { it.performer.role == RoleType.SLUT }
        val blockedIds = blockActions.map { it.target.id }.toSet()

        val mafiaActions = actions.filter { it.performer.role == RoleType.MAFIA }
        val doctorActions = actions.filter { it.performer.role == RoleType.DOCTOR }
        val maniacAction = actions.findLast { it.performer.role == RoleType.MANIAC }

        val mafiaTargetId = mafiaActions.groupingBy { it.target.id }.eachCount().maxByOrNull { it.value }?.key
        val mafiaTarget = players.firstOrNull { it.id == mafiaTargetId }
        val doctorTargetsId = doctorActions.map { it.target.id }

        val wasSaved = mafiaTarget?.id != null && doctorTargetsId.contains(mafiaTarget.id)

        if (mafiaTarget != null) {
            val performers = mafiaActions.filter { it.target.id == mafiaTarget.id }
                .mapNotNull { action -> players.firstOrNull { it.id == action.performer.id } }

            events += NightEvent.KillAttempt(
                performers = performers,
                RoleType.MAFIA,
                target = mafiaTarget,
                wasSaved = wasSaved
            )

            if (!wasSaved) {
                session.eliminatePlayer(mafiaTarget.id)
            }
        }
        doctorActions.forEach {
                doctorAction ->
            if (doctorAction.target.id == mafiaTargetId) {
                val performer = players.firstOrNull { it.id == doctorAction.performer.id }
                val target = players.firstOrNull { it.id == doctorAction.target.id }
                if (performer != null && target != null) {
                    events += NightEvent.DoctorSaved(performer, target)
                }
            }
        }

        if (maniacAction != null) {
            val performer = players.firstOrNull { it.id == maniacAction.performer.id }
            val target = players.firstOrNull { it.id == maniacAction.target.id }
            val wasBlocked = blockedIds.contains(maniacAction.performer.id)

            if (performer != null && target != null) {
                events += NightEvent.ManiacKill(performer, target, wasBlocked)

                if (!wasBlocked) {
                    session.eliminatePlayer(target.id)
                }
            }
        }

        blockActions.forEach { action ->
            val blocker = players.firstOrNull { it.id == action.performer.id }
            val blocked = players.firstOrNull { it.id == action.target.id }
            if (blocker != null && blocked != null) {
                events += NightEvent.SlutBlocked(blocker, blocked)
            }
        }

        clearActions()
        return events
    }
}
