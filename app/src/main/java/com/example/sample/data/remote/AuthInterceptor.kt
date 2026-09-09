package com.example.sample.data.remote

import com.example.sample.data.datastore.DataStoreRepository
import com.example.sample.viewModels.DataStoreViewModel.Companion.TOKEN_KEY
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStore: DataStoreRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            dataStore.getString(TOKEN_KEY)
        }

        val request = chain.request().newBuilder()

        token?.let {
            request.addHeader(name = "Authorization", value = "Bearer $it")
        }

        return chain.proceed(request.build())
    }


}