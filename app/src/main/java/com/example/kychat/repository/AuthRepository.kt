package com.example.kychat.repository

import com.example.kychat.network.LoginRequest
import com.example.kychat.network.NetworkModule

class AuthRepository {
    private val apiService = NetworkModule.apiService

    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.token)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}