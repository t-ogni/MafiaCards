package com.yakovskij.mafia_engine.domain

import com.yakovskij.mafia_engine.domain.role.RoleType

data class Player(
    val id: Int,
    val name: String,
    var role: RoleType? = null,
    var isAlive: Boolean = true,
    var canVote: Boolean = true
)
