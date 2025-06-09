package com.yakovskij.mafiacards.features.game.domain

enum class RoleType(val title: String) {
    SPECTATOR("Призрак"),
    MAFIA("Мафия"),
    DOCTOR("Доктор"),
    DETECTIVE("Детектив"),
    CIVILIAN("Мирный житель");
}

data class RoleCard(
    val type: RoleType,
    val isUnique: Boolean = true
)