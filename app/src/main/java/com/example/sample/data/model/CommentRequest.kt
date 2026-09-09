package com.example.sample.data.model

data class CommentRequest(
    val content: String,
    val parent_id: Int? = null
)