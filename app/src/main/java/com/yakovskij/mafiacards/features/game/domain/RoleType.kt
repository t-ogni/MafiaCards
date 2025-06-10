package com.yakovskij.mafiacards.features.game.domain

enum class RoleType(val title: String, val gender: Gender = Gender.MALE) {
    SPECTATOR("Призрак", Gender.MALE),
    MAFIA("Мафия", Gender.FEMALE),
    DOCTOR("Доктор", Gender.MALE),
    DETECTIVE("Детектив", Gender.MALE),
    MANIAC("Маньяк", Gender.MALE),
    SLUT("Блудница", Gender.FEMALE),
    CIVILIAN("Мирный житель", Gender.MALE);
}

enum class Gender {
    MALE,
    FEMALE,
    NEUTRAL
}