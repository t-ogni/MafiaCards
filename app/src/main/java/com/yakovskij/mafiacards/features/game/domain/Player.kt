package com.yakovskij.mafiacards.features.game.domain

data class Player(
    val id: Int,
    val name: String,
    val role: RoleCard? = null,
    val isAlive: Boolean = true
)
