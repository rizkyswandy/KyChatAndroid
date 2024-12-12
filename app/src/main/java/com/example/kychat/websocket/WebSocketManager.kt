// WebSocketManager.kt
package com.example.kychat.websocket

import android.util.Log
import com.example.kychat.model.Message
import com.google.gson.Gson
import com.google.gson.GsonBuilder  // Add this import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketManager {
    private var webSocket: WebSocket? = null
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // Change to private val and initialize directly
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        .create()

    private fun handleReceivedMessage(text: String) {
        try {
            Log.d("WebSocket", "Received raw message: $text")

            val message = gson.fromJson(text, Message::class.java)
            Log.d("WebSocket", "Parsed message: $message")

            val currentMessages = _messages.value.toMutableList()
            if (!currentMessages.any { it.id == message.id }) {
                currentMessages.add(message)
                _messages.value = currentMessages
                Log.d("WebSocket", "Added new message to list. Total messages: ${currentMessages.size}")
            } else {
                Log.d("WebSocket", "Duplicate message detected, skipping")
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Error parsing message: ${e.message}")
            Log.e("WebSocket", "Raw message was: $text")
            e.printStackTrace()
        }
    }

    fun connect(token: String) {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://your-ip:8080/ws?token=$token")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received message: $text")
                handleReceivedMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Connection failed", t)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Connection closed. Code: $code, Reason: $reason")
            }
        })
    }

    fun sendMessage(content: String) {
        val message = mapOf("content" to content)
        val jsonMessage = gson.toJson(message)
        webSocket?.send(jsonMessage)
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
    }
}