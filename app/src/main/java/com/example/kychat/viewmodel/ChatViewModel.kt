package com.example.kychat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.kychat.model.Message
import com.example.kychat.repository.ChatRepository

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isConnected: Boolean = false,
    val error: String? = null
)

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    init {
        viewModelScope.launch {
            repository.messages.collect { messages ->
                _state.value = _state.value.copy(messages = messages)
            }
        }
    }
    fun connect(token: String) {
        viewModelScope.launch {
            repository.connect(
                token = token,
                onMessage = { message ->
                    _state.value = _state.value.copy(
                        messages = _state.value.messages + message
                    )
                },
                onError = { error ->
                    _state.value = _state.value.copy(error = error)
                },
                onConnectionStateChange = { isConnected ->
                    _state.value = _state.value.copy(isConnected = isConnected)
                }
            )
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            repository.sendMessage(content)
        }
    }
}