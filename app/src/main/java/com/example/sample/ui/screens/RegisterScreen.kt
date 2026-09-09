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
import com.example.sample.viewModels.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit
) {

    val context = LocalContext.current
    val registerResponse by registerViewModel.registerResponse.collectAsState()

    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }


    LaunchedEffect(registerResponse) {
        if (registerResponse is NetworkResult.Success) {
            navigateToHomeScreen()
            Toast.makeText(
                context,
                "ثبت‌نام موفق",
                Toast.LENGTH_LONG
            ).show()
        }

        if (registerResponse is NetworkResult.Error) {
            Toast.makeText(
                context,
                (registerResponse as NetworkResult.Error<AuthData>).message,
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
            value = name,
            onValueChange = { name = it },
            shape = CircleShape,
            singleLine = true,
            label = { Text(text = "اسم", color = Color.Gray) }
        )


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
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password = it },
            shape = CircleShape,
            singleLine = true,
            label = { Text(text = "رمز عبور", color = Color.Gray) }
        )

        OutlinedTextField(
            value = passwordConfirmation,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { passwordConfirmation = it },
            shape = CircleShape,
            singleLine = true,
            label = { Text(text = "تکرار رمز عبور", color = Color.Gray) }
        )

        Button(
            onClick = {
                registerViewModel.register(
                    name = name,
                    mobile = mobile,
                    password = password,
                    passwordConfirmation = passwordConfirmation
                )
            },
            enabled = registerResponse !is NetworkResult.Loading,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(0.5f)
        ) {
            if (registerResponse is NetworkResult.Loading) {
                CircularProgressIndicator()
            } else {
                Text(text = "ثبت‌نام", style = MaterialTheme.typography.titleLarge)
            }
        }
    }

}