package com.example.proyecto.controlador

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.vista.BodyDataScreen
import com.example.proyecto.vista.IMCScreen
import com.example.proyecto.vista.LoginScreen
import com.example.proyecto.vista.RegisterScreen
import com.example.proyecto.vista.TestDetailScreen
import com.example.proyecto.vista.TestEvaluationScreen
import com.example.proyecto.vista.TestListScreen

object ThemeManager {
    var isDarkMode: MutableState<Boolean> = mutableStateOf(false)

    fun toggleTheme() {
        isDarkMode.value = !isDarkMode.value
    }
}

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login")
        { LoginScreen(navController) }

        composable("register")
        { RegisterScreen(navController) }

        composable("body_data")
        { BodyDataScreen(navController) }


        composable("test_list") {
            TestListScreen(navController = navController)
        }

        composable("test_detail_screen/{testName}") { backStackEntry ->
            val testName = backStackEntry.arguments?.getString("testName") ?: ""
            TestDetailScreen(navController = navController, testName = testName)
        }
        composable("test_evaluation/{testName}") { backStackEntry ->
            val testName = backStackEntry.arguments?.getString("testName") ?: ""
            TestEvaluationScreen(navController = navController, testName = testName)
        }

        composable("imc_screen") {
            IMCScreen(navController = navController)
        }


    }

}


