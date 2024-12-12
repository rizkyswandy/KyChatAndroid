package com.example.kychat.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Message(
    @SerializedName("id")
    val id: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("senderId")
    val senderId: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("timestamp")
    val timestamp: String // We'll keep it as String initially and convert as needed
) {
    fun getFormattedTimestamp(): Date? {
        return try {
            // Use ISO8601 format matching the server
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).parse(timestamp)
        } catch (e: Exception) {
            null
        }
    }
}