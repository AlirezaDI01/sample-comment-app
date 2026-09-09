package com.example.sample.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sample.ui.screens.HomeScreen
import com.example.sample.ui.screens.LoginScreen
import com.example.sample.ui.screens.ProfileScreen
import com.example.sample.ui.screens.RegisterScreen
import com.example.sample.ui.screens.WelcomeScreen


@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    innerPadding: PaddingValues
) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {

        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                navigateToRegisterScreen = {
                    navHostController.navigate(Screen.Register.route)
                },
                navigateToLoginScreen = {
                    navHostController.navigate(Screen.Login.route)
                }
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                navigateToHomeScreen = {
                    navHostController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                navigateToHomeScreen = {
                    navHostController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                navigateToProfileScreen = {
                    navHostController.navigate(Screen.Profile.route)
                },
                navigateToWelcomeScreen = {
                    navHostController.navigate(Screen.Welcome.route){
                        popUpTo(navHostController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                navigateToWelcomeScreen = {
                    navHostController.navigate(Screen.Welcome.route) {
                        popUpTo(navHostController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }

}