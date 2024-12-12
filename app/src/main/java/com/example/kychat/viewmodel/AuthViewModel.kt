package com.example.kychat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.kychat.repository.AuthRepository  // Add this import

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val token: String? = null
)

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val result = repository.login(username, password)
                result.fold(
                    onSuccess = { token ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            token = token
                        )
                    },
                    onFailure = { error ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun register(username: String, password: String) {
        // TODO: Implement register
        _state.value = _state.value.copy(isLoading = true)
    }
}