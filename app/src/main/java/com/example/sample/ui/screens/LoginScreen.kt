package com.example.sample.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sample.data.model.AuthData
import com.example.sample.data.remote.NetworkResult
import com.example.sample.viewModels.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit
) {

    val context = LocalContext.current

    val loginResponse by loginViewModel.loginResponse.collectAsState()

    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(loginResponse) {
        if (loginResponse is NetworkResult.Success) {
            navigateToHomeScreen()
            Toast.makeText(
                context,
                "ورود موفق",
                Toast.LENGTH_LONG
            ).show()
        }

        if (loginResponse is NetworkResult.Error) {
            Toast.makeText(
                context,
                (loginResponse as NetworkResult.Error<AuthData>).message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            shape = CircleShape,
            singleLine = true,
            label = { Text(text = "شماره موبایل", color = Color.Gray) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            shape = CircleShape,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "رمز عبور", color = Color.Gray) }
        )

        Button(
            onClick = {
                loginViewModel.login(mobile = mobile, password = password)
            },
            enabled = loginResponse !is NetworkResult.Loading,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(0.5f)
        ) {
            if (loginResponse is NetworkResult.Loading){
                CircularProgressIndicator()
            }else{
                Text(text = "ورود", style = MaterialTheme.typography.titleLarge)
            }
        }
    }

}