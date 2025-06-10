package com.yakovskij.mafiacards.features.game.domain

data class NightAction(
    val performer: Player,
    val target: Player,
    var result: NightResult = NightResult.NONE // по умолчанию
)

enum class NightResult {
    NONE,           // ещё не обработан
    KILLED,         // цель убита
    SAVED,          // цель спасена
    FAILED,         // действие не дало эффекта
    SUCCESS,         // действие выполнено
    BLOCKED         // (если добавишь роль блокера)
}
