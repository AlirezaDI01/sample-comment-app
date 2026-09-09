package com.example.sample.viewModels

import android.content.Context
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.data.model.AuthData
import com.example.sample.data.model.RegisterRequest
import com.example.sample.data.remote.NetworkResult
import com.example.sample.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiRepository: ApiRepository
) : ViewModel() {


     private val _registerResponse = MutableStateFlow<NetworkResult<AuthData>?>(null)
     val registerResponse = _registerResponse.asStateFlow()

    fun register(name: String, mobile: String, password: String, passwordConfirmation: String) {

        if (name.isBlank()) {
            Toast.makeText(context, "اسم خالی است", Toast.LENGTH_SHORT).show()
            return
        }

        if (mobile.isBlank() || mobile.length != 11 || !mobile.isDigitsOnly()) {
            Toast.makeText(context, "شماره موبایل نادرست است", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(context, "رمز حداقل باید 8 کاراکتر باشد", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordConfirmation) {
            Toast.makeText(context, "تکرار رمز اشتباه است", Toast.LENGTH_SHORT).show()
            return
        }

        val registerRequest = RegisterRequest(
            name = name,
            mobile = mobile,
            password = password,
            password_confirmation = passwordConfirmation
        )


        viewModelScope.launch {
            _registerResponse.emit(NetworkResult.Loading())

            val result = apiRepository.register(registerRequest)
            _registerResponse.emit(result)

        }
    }

}
