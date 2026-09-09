package com.example.sample.data.model

data class ResponseResult<T>(
    val success : Boolean,
    val message : String?,
    val data : T?,
    val errors: Map<String, List<String>>? = null
)

