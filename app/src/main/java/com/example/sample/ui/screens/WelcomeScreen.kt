package com.example.sample.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    navigateToRegisterScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(text = "خوش آمدید!", style = MaterialTheme.typography.displayMedium)

        //login button
        Button(
            onClick = navigateToLoginScreen,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(vertical = 4.dp)
        ) {
            Text(text = "ورود", style = MaterialTheme.typography.titleLarge)
        }

        //register button
        OutlinedButton(
            onClick = navigateToRegisterScreen,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(vertical = 4.dp)
        ) {
            Text(text = "ثبت‌نام", style = MaterialTheme.typography.titleLarge)
        }


    }
}


