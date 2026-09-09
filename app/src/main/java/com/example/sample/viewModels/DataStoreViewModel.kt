package com.example.sample.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.data.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStore: DataStoreRepository
) : ViewModel() {

    companion object {
        const val TOKEN_KEY = "token"
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.putString(key = TOKEN_KEY, value = token)
        }
    }

    fun getToken() : String = runBlocking {
        dataStore.getString(TOKEN_KEY) ?: ""
    }

}