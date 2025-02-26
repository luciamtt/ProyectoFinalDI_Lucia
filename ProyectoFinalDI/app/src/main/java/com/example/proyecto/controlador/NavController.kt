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

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }
        composable("bodyData") { BodyData(navController) }
        composable("someOtherScreen") {
            // Aquí iría la siguiente pantalla que deseas que se muestre después de guardar los datos
        }
    }
}
