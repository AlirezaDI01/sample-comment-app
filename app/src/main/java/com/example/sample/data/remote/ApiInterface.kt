package com.example.sample.data.remote

import com.example.sample.data.model.AuthData
import com.example.sample.data.model.Comment
import com.example.sample.data.model.CommentRequest
import com.example.sample.data.model.LoginRequest
import com.example.sample.data.model.RegisterRequest
import com.example.sample.data.model.ResponseResult
import com.example.sample.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ResponseResult<AuthData>>


    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseResult<AuthData>>

    @GET("api/auth/me")
    suspend fun getMe(): Response<ResponseResult<User>>

    @GET("api/comments")
    suspend fun getComments(): Response<ResponseResult<List<Comment>>>

    @POST("api/comments")
    suspend fun postComment(@Body request: CommentRequest): Response<ResponseResult<Comment>>
}