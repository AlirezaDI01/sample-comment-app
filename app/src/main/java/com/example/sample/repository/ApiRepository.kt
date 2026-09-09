package com.example.sample.repository

import com.example.sample.data.datastore.DataStoreRepository
import com.example.sample.data.model.AuthData
import com.example.sample.data.model.Comment
import com.example.sample.data.model.CommentRequest
import com.example.sample.data.model.LoginRequest
import com.example.sample.data.model.RegisterRequest
import com.example.sample.data.model.User
import com.example.sample.data.remote.ApiInterface
import com.example.sample.data.remote.BaseApiResponse
import com.example.sample.data.remote.NetworkResult
import com.example.sample.viewModels.DataStoreViewModel.Companion.TOKEN_KEY
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val api: ApiInterface,
    private val dataStore: DataStoreRepository
) : BaseApiResponse() {


    suspend fun register(registerRequest: RegisterRequest): NetworkResult<AuthData> {
        val result = safeApiCall {
            api.register(request = registerRequest)
        }

        if (result is NetworkResult.Success && result.data != null) {
            dataStore.putString(TOKEN_KEY, result.data.token)
        }

        return result
    }


    suspend fun login(loginRequest: LoginRequest): NetworkResult<AuthData> {
        val result = safeApiCall {
            api.login(loginRequest)
        }

        if (result is NetworkResult.Success && result.data != null) {
            dataStore.putString(TOKEN_KEY, result.data.token)
        }

        return result
    }

    suspend fun getMe(): NetworkResult<User> = safeApiCall {
        api.getMe()
    }

    suspend fun postComment(commentRequest: CommentRequest): NetworkResult<Comment> = safeApiCall {
        api.postComment(request = commentRequest)
    }


    suspend fun getComments(): NetworkResult<List<Comment>> = safeApiCall {
        api.getComments()
    }

}