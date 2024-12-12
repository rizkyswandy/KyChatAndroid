// app/src/main/java/com/example/chatapp/MainActivity.kt
package com.example.kychat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kychat.navigation.ChatNavigation
import com.example.kychat.ui.theme.ChatAppTheme
import com.example.kychat.viewmodel.ChatViewModel
import androidx.compose.foundation.layout.fillMaxSize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ChatViewModel = viewModel()
                    ChatNavigation(viewModel)
                }
            }
        }
    }
}

@Composable
fun ChatApp() {
    ChatAppTheme {
        val viewModel: ChatViewModel = viewModel()
        ChatNavigation(viewModel)
    }
}