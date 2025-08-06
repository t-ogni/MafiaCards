package com.yakovskij.mafia_engine.domain

enum class GamePhase {
    SETUP,
    DAY_WITHOUT_VOTING,
    NIGHT,
    NIGHT_ENDED,
    DAY_DISCUSSION,
    VOTING,
    VOTING_ENDED,
    END
}
