package com.yakovskij.mafia_engine.domain.role


enum class RoleType(
    val title: String,
    val side: TeamSide,
    val priority: RolePriority
) {
    SPECTATOR("Призрак", TeamSide.NEUTRAL, RolePriority.LOW),
    MAFIA("Мафия", TeamSide.MAFIA, RolePriority.HIGH),
    DOCTOR("Доктор", TeamSide.CIVILIANS, RolePriority.MEDIUM),
    DETECTIVE("Детектив", TeamSide.CIVILIANS, RolePriority.MEDIUM),
    MANIAC("Маньяк", TeamSide.MANIACS, RolePriority.HIGH),
    SLUT("Блудница", TeamSide.CIVILIANS, RolePriority.LOW),
    CIVILIAN("Мирный житель", TeamSide.CIVILIANS, RolePriority.LOW);
}