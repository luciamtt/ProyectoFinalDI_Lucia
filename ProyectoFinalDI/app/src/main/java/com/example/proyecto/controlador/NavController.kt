package com.example.proyecto.controlador

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto.vista.LoginScreen
import com.example.proyecto.vista.RegisterScreen

@Composable
fun appNav(navController: NavHostController){
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(navController) {
                navController.navigate("home")
            }
        }
        composable("register"){
            RegisterScreen  { username, password -> navController.popBackStack() }
        }
    }
}