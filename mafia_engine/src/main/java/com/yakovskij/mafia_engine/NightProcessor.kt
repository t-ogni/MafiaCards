package com.yakovskij.mafia_engine

import com.yakovskij.mafia_engine.domain.*
import kotlin.collections.mutableListOf

class NightProcessor(private var session: GameSession) {
    private val actions = mutableListOf<NightAction>()
    private val events = mutableListOf<NightEvent>()

    fun addAction(performerId: Int, targetId: Int) {
        val performer = session.state.players.firstOrNull { it.id == performerId }!!

        if (!performer.isAlive) throw GameException.DeadPlayerAction()

        val target = session.state.players.firstOrNull { it.id == targetId }
            ?: throw GameException.InvalidTarget()

        if (!target.isAlive) throw GameException.TargetIsDead()

        actions.removeAll { it.performer.id == performer.id && it.performer.role == performer.role }
        actions.add(NightAction(performer, target))
    }

    fun getActions(): List<NightAction> = actions.toList()
    fun getLastNightEvents(): List<NightEvent> = events.toList()

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
        clearEvents()
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
                    events += NightEvent.Saved(performer, target)
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
                events += NightEvent.Blocked(blocker, blocked)
            }
        }

        clearActions()
        return events
    }

    fun formatNightEvent(event: NightEvent): String {
        return when (event) {
            is NightEvent.KillAttempt -> {
                val killers = event.performers.joinToString(", ") { it.role?.title ?: it.name }
                if (event.wasSaved)
                    "$killers попытались убить ${event.target.name}, но его спасли."
                else
                    "$killers убили ${event.target.name}."
            }

            is NightEvent.Saved -> {
                "${event.performer.role?.title ?: event.performer.name} спас ${event.target.name}."
            }

            is NightEvent.FailedAction -> {
                "${event.performer.role?.title ?: event.performer.name} пытался воздействовать на ${event.target.name}, но это не сработало."
            }

            is NightEvent.Blocked -> {
//                val performerSex = when(event.blocker.role?.gender) {
//                    Gender.MALE -> "провёл"
//                    Gender.FEMALE -> "провела"
//                    Gender.NEUTRAL -> "провело"
//                }

                "${event.blocker.role?.title} провёл(а) ночь с ${event.blocked.name}, и тот(та) не смог(ла) действовать."
            }

            is NightEvent.ManiacKill -> {
                if (event.wasBlocked)
                    "Маньяк пытался убить ${event.target.name}, но его отвлекли."
                else
                    "Маньяк убил ${event.target.name}."
            }

            is NightEvent.Custom -> event.description
        }
    }
}
