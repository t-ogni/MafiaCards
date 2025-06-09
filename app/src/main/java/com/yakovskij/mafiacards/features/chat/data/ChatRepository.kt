package com.yakovskij.mafiacards.features.chat.data

import com.yakovskij.mafiacards.features.chat.domain.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatRepository @Inject constructor() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(text: String, sender: String) {
        val msg = ChatMessage(sender, text, System.currentTimeMillis())
        // отправка по сети...
        _messages.value += msg
    }

    fun receiveIncoming(msg: ChatMessage) {
        _messages.value += msg
    }
}
