package com.example.sample.data.remote

import com.example.sample.data.model.ResponseResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

open class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<ResponseResult<T>>): NetworkResult<T> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        return@withContext NetworkResult.Success(
                            message = body.message,
                            data = body.data!!
                        )
                    }
                }


                val errorBodyString = response.errorBody()?.string()
                val errorResponse = try {
                    Gson().fromJson(errorBodyString, ResponseResult::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                val baseMessage = errorResponse?.message ?: "Error: ${response.code()}"

                val detailedError = errorResponse?.errors?.let { errorsMap ->
                    val sb = StringBuilder(baseMessage)
                    errorsMap.forEach { (field, messages) ->
                        sb.append("\n$field: ${messages.joinToString(", ")}")
                    }
                    sb.toString()
                } ?: baseMessage


                return@withContext error(errorMessage = detailedError)

            } catch (e: Exception) {
                return@withContext error(errorMessage = e.message ?: e.toString())
            }
        }


    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(message = errorMessage)
}