package com.yakovskij.mafiacards.features.chat.domain

data class ChatMessage(
    val sender: String,
    val content: String,
    val timestamp: Long,
    val isSystem: Boolean = false
)