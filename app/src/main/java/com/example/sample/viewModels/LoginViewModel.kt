package com.example.sample.viewModels

import android.content.Context
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.data.model.AuthData
import com.example.sample.data.model.LoginRequest
import com.example.sample.data.remote.NetworkResult
import com.example.sample.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiRepository: ApiRepository
) : ViewModel() {


    private val _loginResponse = MutableStateFlow<NetworkResult<AuthData>?>(null)
    val loginResponse = _loginResponse.asStateFlow()

    fun login(mobile: String, password: String) {
        if (mobile.isBlank() || mobile.length != 11 || !mobile.isDigitsOnly()) {
            Toast.makeText(context, "شماره موبایل نادرست است", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isBlank()) {
            Toast.makeText(context, "رمز را وارد کنید", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(
            mobile = mobile,
            password = password
        )

        viewModelScope.launch {
            _loginResponse.emit(NetworkResult.Loading())

            val result = apiRepository.login(loginRequest = loginRequest)
            _loginResponse.emit(result)
        }

    }


}