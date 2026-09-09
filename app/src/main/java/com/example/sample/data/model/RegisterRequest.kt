package com.example.sample.data.model

data class RegisterRequest(
    val name: String,
    val mobile: String,
    val password: String,
    val password_confirmation: String
)
