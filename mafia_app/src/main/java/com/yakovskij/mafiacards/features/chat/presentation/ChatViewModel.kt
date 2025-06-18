package com.yakovskij.mafiacards.features.chat.presentation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.yakovskij.mafiacards.features.chat.data.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    val messages = chatRepository.messages

    fun onSend(text: String) {
        chatRepository.sendMessage(text, sender = "Я")
    }
}
