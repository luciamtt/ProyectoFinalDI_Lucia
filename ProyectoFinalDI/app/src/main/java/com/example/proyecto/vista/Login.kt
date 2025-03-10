package com.example.proyecto.vista

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Iniciar Sesión", fontSize = 32.sp, color = Color(0xFF6200EE), modifier = Modifier.padding(bottom = 32.dp))

            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), singleLine = true)

            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation())

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    val savedUsername = sharedPreferences.getString("username", null)
                    val savedPassword = sharedPreferences.getString("password", null)

                    if (savedUsername == username && savedPassword == password) {
                        navController.navigate("body_data") // Redirige a BodyDataScreen
                    } else {
                        errorMessage = "Usuario o contraseña incorrectos"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Iniciar sesión", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("register") }) {
                Text(text = "¿No tienes cuenta? Regístrate aquí", color = Color(0xFF6200EE), fontSize = 16.sp)
            }
        }
    }
}
