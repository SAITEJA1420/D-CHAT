package com.example.d_chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.d_chat.ui.theme.DChatTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DChatTheme {
                val viewModel = remember { DChatViewModel() }
                var currentScreen by remember { mutableStateOf(viewModel.currentScreen) }

                BackHandler(enabled = currentScreen != "welcome") {
                    when (currentScreen) {
                        "login", "signup" -> currentScreen = "welcome"
                        "Start Chat" -> currentScreen = "login"
                        "chat screen" -> {
                            viewModel.chatOpened = false
                            viewModel.receiverId = 0
                            currentScreen = "Start Chat"
                        }
                    }
                }

                if(currentScreen == "welcome"){
                    WelcomeScreen(onNavigate = {currentScreen = it})
                }
                if (currentScreen == "login") {
                    LoginScreen(onLoginSubmit = { userName, password -> viewModel.loginCall(userName, password) },
                        onSignupSubmit = { currentScreen = "signup" },
                        loginMessage =  viewModel.loginMessage
                    )
                    LaunchedEffect(viewModel.loginMessage) {
                        if (viewModel.loginMessage.isNotEmpty()) {
                            delay(2000)
                            viewModel.clearMessage()
                        }
                    }
                    LaunchedEffect(viewModel.loginState) {
                        if(viewModel.loginState){
                            currentScreen = "Start Chat"
                        }
                    }
                }
                if (currentScreen == "signup") {
                    SignupScreen(onSignupSubmit = { userName, password -> viewModel.signupCall(userName, password) },
                        signupMessage = viewModel.signupMessage
                    )
                    LaunchedEffect(viewModel.signupMessage) {
                        if (viewModel.signupMessage.isNotEmpty()) {
                            delay(2000)
                            viewModel.clearMessage()
                        }
                    }
                    LaunchedEffect(viewModel.signupState) {
                        if (viewModel.signupState) {
                            currentScreen = "login"
                        }
                    }
                }
                if(currentScreen == "Start Chat"){
                    LaunchedEffect(Unit) { viewModel.fetchAllUsers() }
                    NewChat(allUsers = viewModel.allUsers,
                        onUsernameSubmit = {userName ->  viewModel.currentUser = userName},
                        onIdSubmit = {receiverId -> viewModel.receiverId = receiverId},
                    )
                    if(viewModel.receiverId!= 0){
                        viewModel.messageUser(viewModel.receiverId)
                    }
                    if(viewModel.chatOpened){
                        currentScreen = "chat screen"
                    }
                }
                if(currentScreen == "chat screen"){
                    LaunchedEffect(viewModel.userId, viewModel.receiverId) {
                        if(viewModel.userId != 0 && viewModel.receiverId != 0){
                            viewModel.fetchChatHistory(viewModel.userId, viewModel.receiverId)
                        }
                    }
                    ChatScreen(user = viewModel.currentUser,
                        onMessageSubmit = {message -> viewModel.sendMessage(message)},
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

