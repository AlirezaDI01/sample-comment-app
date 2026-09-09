package com.example.sample.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.data.datastore.DataStoreRepository
import com.example.sample.data.model.User
import com.example.sample.data.remote.NetworkResult
import com.example.sample.repository.ApiRepository
import com.example.sample.viewModels.DataStoreViewModel.Companion.TOKEN_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _user = MutableStateFlow<NetworkResult<User>>(NetworkResult.Loading())
    val user = _user
        .onStart { getMe() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            NetworkResult.Loading()
        )

    fun getMe() {
        viewModelScope.launch {
            _user.emit(NetworkResult.Loading())
            val result = apiRepository.getMe()
            _user.emit(result)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _user.emit(NetworkResult.Loading())
            dataStore.putString(TOKEN_KEY, "")
            getMe()
        }
    }

}