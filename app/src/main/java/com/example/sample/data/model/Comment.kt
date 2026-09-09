package com.example.sample.data.model

data class Comment(
    val id: Int,
    val content: String,
    val user: User,
    val created_at: String,
    val replies: List<Comment>? = null
)