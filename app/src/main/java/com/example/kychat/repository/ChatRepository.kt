package com.example.kychat.repository

import com.example.kychat.websocket.WebSocketManager
import com.example.kychat.model.Message
import kotlinx.coroutines.flow.StateFlow

class ChatRepository {
    private val webSocketManager = WebSocketManager()

    val messages: StateFlow<List<Message>> = webSocketManager.messages

    suspend fun connect(
        token: String,
        onMessage: (Message) -> Unit,
        onError: (String) -> Unit,
        onConnectionStateChange: (Boolean) -> Unit
    ) {
        try {
            webSocketManager.connect(token)
            onConnectionStateChange(true)
        } catch (e: Exception) {
            onError(e.message ?: "Connection failed")
            onConnectionStateChange(false)
        }
    }

    suspend fun sendMessage(content: String) {
        webSocketManager.sendMessage(content)
    }
}