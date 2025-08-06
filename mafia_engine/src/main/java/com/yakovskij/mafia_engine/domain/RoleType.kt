package com.yakovskij.mafia_engine.domain

enum class RoleType(val title: String, val gender: Gender = Gender.MALE) {
    SPECTATOR("Призрак", Gender.MALE),
    MAFIA("Мафия", Gender.FEMALE),
    DOCTOR("Доктор", Gender.MALE),
    DETECTIVE("Детектив", Gender.MALE),
    MANIAC("Маньяк", Gender.MALE),
    SLUT("Блудница", Gender.FEMALE),
    CIVILIAN("Мирный житель", Gender.MALE);
}

// для грамматики предложений русского языка.
enum class Gender {
    MALE,
    FEMALE,
    NEUTRAL
}

// todo: приоритеты, по которым будут удаляться наменее приоритетные роли из колоды
// todo: enum с указанием стороны команды - мирные \ мафия \ маньяки
//  возможно сторона переходящие (для возможных будущих ролей двуликого и тп). А может сделать две роли (TransitorCiv \ TransitorMaf) и менять исходя из хода игры.
