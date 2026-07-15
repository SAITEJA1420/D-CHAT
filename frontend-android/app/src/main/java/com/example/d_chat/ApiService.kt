package com.example.d_chat

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/signup")
    suspend fun signupUser(@Body request: SignupRequest): Response<SignupResponse>

    @POST("/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/allUsers")
    suspend fun allUsers(): Response<AllUsersResponse>

    @GET("/chat-history/{sender_id}/{receiver_id}")
    suspend fun getChatHistory(
        @Path("sender_id") senderId: Int,
        @Path("receiver_id") receiverId: Int
    ): Response<ChatHistoryResponse>

}