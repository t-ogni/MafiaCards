package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults.formatter

import com.yakovskij.mafia_engine.domain.NightEvent

class NightEventFormatter {
    fun format(events: List<NightEvent>): List<String> {
        return events.map { event ->
            when (event) {
                is NightEvent.KillAttempt -> {
                    val mafiaNames = event.performers.joinToString(", ") { it.name }
                    if (event.wasSaved) {
                        "Мафия ($mafiaNames) пыталась убить игрока ${event.target.name}, но Доктор спас его."
                    } else {
                        "Мафия ($mafiaNames) убила игрока ${event.target.name}."
                    }
                }

                is NightEvent.Saved -> "Доктор пытался спасти игрока ${event.target.name}, но его никто не трогал."

                is NightEvent.FailedAction -> "${event.roleType.title} пытался воздействовать на ${event.target.name}, но это не сработало."

                is NightEvent.Blocked -> "${event.blocker.name} заблокировал действия ${event.blocked.name}."

                is NightEvent.ManiacKill -> {
                    if (event.wasBlocked) {
                        "Маньяк (${event.performer.name}) хотел убить ${event.target.name}, но его заблокировали."
                    } else {
                        "Маньяк (${event.performer.name}) убил ${event.target.name}."
                    }
                }

                is NightEvent.Custom -> event.description
            }
        }
    }
}
