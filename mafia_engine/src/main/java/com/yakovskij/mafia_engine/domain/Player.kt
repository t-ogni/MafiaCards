package com.yakovskij.mafia_engine.domain

data class Player(
    val id: Int,
    val name: String,
    val role: RoleType? = null,
    val isAlive: Boolean = true
)
