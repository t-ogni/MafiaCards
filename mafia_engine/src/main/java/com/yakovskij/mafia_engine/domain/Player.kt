package com.yakovskij.mafia_engine.domain

data class Player(
    val id: Int,
    val name: String,
    var role: RoleType? = null,
    var isAlive: Boolean = true,
    var canVote: Boolean = true
)
