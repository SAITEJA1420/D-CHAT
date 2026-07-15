package com.example.d_chat

import androidx.core.app.NotificationCompat

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val success: Boolean, val message: String, val token: String, val userid: Int)
data class SignupRequest(val username: String, val password: String)
data class SignupResponse(val success: Boolean, val message: String)
data class User(val id: Int, val username: String)
data class AllUsersResponse(val success: Boolean, val users: List<User>)
data class Message(val sender_id : Int, val receiver_id: Int, val content: String, val timestamp: String)
data class ChatHistoryResponse(val messages: List<Message>)