package com.example.kychat.ui.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kychat.viewmodel.AuthViewModel

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()

    // Debug state changes
    LaunchedEffect(state) {
        Log.d(TAG, """
            State Update:
            Loading: ${state.isLoading}
            Error: ${state.error}
            Token: ${state.token?.take(10)}...
            Username Length: ${username.length}
            Password Set: ${password.isNotEmpty()}
            Mode: ${if (isRegistering) "Registration" else "Login"}
        """.trimIndent())
    }

    // Debug authentication success
    LaunchedEffect(state.token) {
        state.token?.let { token ->
            Log.d(TAG, "Authentication successful. Token length: ${token.length}")
            onLoginSuccess(token)
        }
    }

    // Debug error states
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Log.e(TAG, "Authentication error: $error")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegistering) "Create Account" else "Welcome Back",
            style = MaterialTheme.typography.headlineMedium
        )

        TextField(
            value = username,
            onValueChange = {
                username = it
                Log.d(TAG, "Username updated. Length: ${it.length}")
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                Log.d(TAG, "Password updated. Length: ${it.length}")
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d(TAG, """
                    Attempting ${if (isRegistering) "registration" else "login"}:
                    Username length: ${username.length}
                    Password set: ${password.isNotEmpty()}
                """.trimIndent())

                if (isRegistering) {
                    viewModel.register(username, password)
                } else {
                    viewModel.login(username, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading && username.isNotEmpty() && password.isNotEmpty()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (isRegistering) "Register" else "Login")
            }
        }

        TextButton(
            onClick = {
                isRegistering = !isRegistering
                Log.d(TAG, "Switched to ${if (isRegistering) "registration" else "login"} mode")
            }
        ) {
            Text(
                if (isRegistering) "Already have an account? Login"
                else "Need an account? Register"
            )
        }

        // Show error message if present
        state.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}