package com.example.kychat.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("token")
    val token: String
)

interface APIService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    companion object {
        const val BASE_URL = "http://10.63.59.227:8080/"
    }
}