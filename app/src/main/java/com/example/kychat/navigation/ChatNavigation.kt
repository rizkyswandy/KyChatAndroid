package com.example.kychat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kychat.ui.login.LoginScreen
import com.example.kychat.viewmodel.ChatViewModel
import com.example.kychat.ui.chat.ChatScreen

@Composable
fun ChatNavigation(viewModel: ChatViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { token ->
                    viewModel.connect(token)
                    navController.navigate("chat") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("chat") {
            ChatScreen(viewModel = viewModel)  // Fixed: Correct parameter syntax and passing the existing viewModel
        }
    }
}