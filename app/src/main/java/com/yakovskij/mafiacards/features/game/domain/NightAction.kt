package com.yakovskij.mafiacards.features.game.domain

data class NightAction(
    val performerId: Int,
    val targetId: Int,
    val roleType: RoleType,
    var result: NightResult = NightResult.NONE // по умолчанию
)

enum class NightResult {
    NONE,           // ещё не обработан
    KILLED,         // цель убита
    SAVED,          // цель спасена
    FAILED,         // действие не дало эффекта
    BLOCKED         // (если добавишь роль блокера)
}
