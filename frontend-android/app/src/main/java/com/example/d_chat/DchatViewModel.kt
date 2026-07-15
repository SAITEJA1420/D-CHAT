package com.example.d_chat

import androidx.collection.mutableObjectFloatMapOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import io.socket.client.IO
import io.socket.client.Socket
import retrofit2.converter.gson.GsonConverterFactory

class DChatViewModel : ViewModel() {
    var currentScreen by mutableStateOf("welcome")
    var signupState by mutableStateOf(false)
    var signupMessage by mutableStateOf("")
    var loginState by mutableStateOf(false)
    var loginMessage by mutableStateOf("")
    var authToken by mutableStateOf("")
    var allUsers by mutableStateOf(listOf<User>())
    var currentUser by mutableStateOf("")
    var userId by mutableIntStateOf(0)
    var receiverId by mutableIntStateOf(0)
    var message by mutableStateOf("")
    var chatOpened by mutableStateOf(false)
    var chatHistory by mutableStateOf(listOf<Message>())
    private var socket: Socket? = null

    fun clearMessage() {
        loginMessage = ""
        signupMessage = ""
    }

    fun signupCall(userName: String, password: String) {
        viewModelScope.launch {
            try {
                val request = SignupRequest(username = userName, password = password)
                val response = RetrofitInstance.api.signupUser(request)
                if(response.body()?.success == true){
                    signupState = true
                    signupMessage = response.body()?.message ?: "Login to your account"

                }else if(response.body()?.success == false){
                    signupState = false
                    signupMessage = response.body()?.message ?: "Username already exists"
                }
            } catch(e: Exception) {
                signupState = false
                signupMessage = "No internet or server crashed"
            }
        }
    }
    fun loginCall(userName: String, password: String) {
        viewModelScope.launch {
            try{
                val request = LoginRequest(username = userName, password = password)
                val response = RetrofitInstance.api.loginUser(request)
                if(response.body()?.success == true){
                    loginState =  true
                    loginMessage = response.body()?.message ?: "Login Successful"
                    authToken = response.body()?.token ?: ""
                    userId = response.body()?.userid ?: 0
                }else if(response.body()?.success == false) {
                    loginState = false
                    loginMessage = response.body()?.message ?:""
                }
            } catch(e: Exception) {
                loginState = false
                loginMessage = "No internet or server crashed"
            }
        }
    }
    fun fetchAllUsers(){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.allUsers()
                if(response.isSuccessful){
                    allUsers = response.body()?.users ?:emptyList()
                }
            }catch(e: Exception) { }
        }
    }
    fun connectWebSocket(userId: Int, receiverId: Int, authToken: String) {
        try {
            socket = IO.socket("http://10.0.2.2:5000")

            socket?.on(Socket.EVENT_CONNECT) {
                println("Web socket connected")

                socket?.emit("connect_user", mapOf(
                    "sender_id" to userId,
                    "receiver_id" to receiverId,
                    "token" to authToken
                ))

                socket?.on("receive_message") { args ->
                    println("Received message event!")
                    println("Args: $args")

                    if(args.isNotEmpty()) {
                        val data = args[0] as? Map<String, Any>
                        println("Data: $data")

                        val senderIdReceived = (data?.get("sender_id") as? Number)?.toInt() ?: 0
                        val content = data?.get("content") as? String ?: ""

                        println("Parsed - sender: $senderIdReceived, content: $content")

                        val newMsg = Message(senderIdReceived, receiverId, content, "now")
                        chatHistory = chatHistory + newMsg
                        println("Updated chatHistory, size: ${chatHistory.size}")
                    }
                }

            }

            socket?.connect()
            println("Socket connecting...")
        } catch(e: Exception) {
            // Handle error
        }
    }
    fun messageUser(receiverId: Int) {
        chatOpened = true
        this.receiverId = receiverId
        connectWebSocket(userId, receiverId, authToken)
    }
    fun sendMessage(message: String) {
        if(socket?.connected() ==  true){
            val messageData = mapOf(
                "sender_id" to userId,
                "receiver_id" to receiverId,
                "content" to message,
                "token" to authToken
            )
            socket?.emit("send_message", Gson().toJson(messageData))
        } else{
            connectWebSocket(userId, receiverId, authToken)
        }
    }
    fun fetchChatHistory(senderId: Int, receiverId: Int) {
        viewModelScope.launch {
            try {
                println("Starting fetch...")
                val response = RetrofitInstance.api.getChatHistory(senderId, receiverId)
                println("Got response: code=${response.code()}, isSuccessful=${response.isSuccessful}")

                if(response.isSuccessful) {
                    println("Response body: ${response.body()}")
                    chatHistory = response.body()?.messages ?: emptyList()
                    println("Chat history: ${chatHistory.size} messages")
                } else {
                    println("Failed: ${response.errorBody()?.string()}")
                }
            } catch(e: Exception) {
                println("Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
