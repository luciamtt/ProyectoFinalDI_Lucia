package com.example.proyecto.controlador

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.vista.BodyData
import com.example.proyecto.vista.LoginScreen
import com.example.proyecto.vista.RegisterScreen



@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login")
        { LoginScreen(navController) }
        composable("register")
        { RegisterScreen(navController) }
        composable("body_data")
        { BodyData(navController) }
    }

}
