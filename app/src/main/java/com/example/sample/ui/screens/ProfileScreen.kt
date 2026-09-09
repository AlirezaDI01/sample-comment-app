package com.example.sample.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sample.data.remote.NetworkResult
import com.example.sample.viewModels.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navigateToWelcomeScreen: () -> Unit
) {


    val user by profileViewModel.user.collectAsState()
    val context = LocalContext.current



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (user) {
            is NetworkResult.Success -> {
                Icon(
                    imageVector = Icons.Filled.AccountCircle, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier
                        .size(128.dp)
                        .padding(4.dp)
                )

                Text(text = "نام کاربری : ${user.data?.name}")
                Text(text = "شماره موبایل : ${user.data?.mobile}")

                Spacer(Modifier.height(32.dp))

                OutlinedButton(onClick = { profileViewModel.logOut() }) {
                    Text(text = "خروج از حساب کاربری")
                }
            }

            is NetworkResult.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResult.Error -> {
                Toast.makeText(
                    context,
                    (user as NetworkResult.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                navigateToWelcomeScreen()
            }

        }


    }

}