package com.yakovskij.mafia_engine.presentation

import com.yakovskij.mafia_engine.domain.NightEvent
import com.yakovskij.mafia_engine.domain.RoleType

class NightFormatter {
    fun format(events: List<NightEvent>): List<String> {
        if (events.isEmpty()) return listOf("Ночь прошла тихо. Ни единого шороха.")

        var strings = mutableListOf<String>()

        val kills = mutableListOf<NightEvent.KillAttempt>()
        val saves = mutableListOf<NightEvent.DoctorSaved>()
        val detectives = mutableListOf<NightEvent.DetectiveChecked>()
        val blocks = mutableListOf<NightEvent.SlutBlocked>()
        val maniacKills = mutableListOf<NightEvent.ManiacKill>()
        val fails = mutableListOf<NightEvent.FailedAction>()
        val customs = mutableListOf<NightEvent.Custom>()
        val others = mutableListOf<NightEvent>()

        for (event in events) {
            when (event) {
                is NightEvent.KillAttempt -> kills.add(event)
                is NightEvent.DoctorSaved -> saves.add(event)
                is NightEvent.DetectiveChecked -> detectives.add(event)
                is NightEvent.SlutBlocked -> blocks.add(event)
                is NightEvent.ManiacKill -> maniacKills.add(event)
                is NightEvent.FailedAction -> fails.add(event)
                is NightEvent.Custom -> customs.add(event)
                else -> others.add(event)
            }
        }

        // -- 1. Убийства
        val actualKills = kills.filterNot { it.wasSaved }
        if (actualKills.isNotEmpty()) {
            actualKills.groupBy { it.target }.forEach { target, killAttempts ->
                val attackers = actualKills.filter { it.target == target }
                val attackerGroupings = attackers.groupBy { it.performerGroup }.keys.map { it.title }
                strings.add("Руками группировки ${attackerGroupings.joinToString(", ")}, был убит ${target.name}.")
            }
        }

        // -- 2. Неудачные попытки убийства (из-за спасения)
        val failedKills = kills.filter { it.wasSaved }
        if (failedKills.isNotEmpty()) {
            failedKills.groupBy { it.target }.forEach { target, killAttempts ->
                val attackers = failedKills.filter { it.target == target }
                val attackerGroupings = attackers.groupBy { it.performerGroup }.keys.map { it.title }
                strings.add("${attackerGroupings.joinToString(", ")} попытались убить ${target.name}, но его спасли.")
            }
        }

        // -- 3. Спасения (только если не дублируют выше)
        val uniqueSaves = saves.filterNot { save -> failedKills.any { it.target == save.target } }
        if (uniqueSaves.isNotEmpty()) {
            uniqueSaves.groupBy { it.target }.forEach { target, saves ->
                val savers = uniqueSaves.filter { it.target == target }.map { it.performer.role?.title }
                strings.add("${savers.joinToString(", ")} пришёл на помощь ${target.name}, но это оказалось зря.")
            }
        }

        // -- 4. Раскрытия роли (Детектив)
        val detectiveChecks = detectives.groupBy { it.target }
        if (detectiveChecks.isNotEmpty()) {
            detectiveChecks.forEach { target, check ->
                strings.add("Детектив проверил ночью игрока ${target.name} и узнал его роль - ${target.role?.title}.")
            }
        }

        // -- Неудачные действия
        if (fails.isNotEmpty()) {
            for (fail in fails) {
                when (fail.performer.role) {
                    RoleType.DETECTIVE -> strings.add("Детектив не смог выяснить роль игрока ${fail.target.name}. Его действия были сорваны.")
                    else -> strings.add("${fail.performer.role?.title} не смог выполнить своё действие на ${fail.target.name}. Его действия были сорваны.")
                }
            }
        }

        // -- Кастомные события
        customs.forEach { strings.add("📢 ${it.description}") }

        return strings
    }
}
