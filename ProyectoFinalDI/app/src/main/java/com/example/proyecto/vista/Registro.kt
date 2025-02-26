package com.example.proyecto.vista;

import androidx.compose.ui.tooling.preview.Preview
import android.content.Context
import android.content.SharedPreferences
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
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class User(val username: String, val password: String)

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) }
    val gson = Gson()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse", fontSize = 32.sp, color = Color(0xFF6200EE), modifier = Modifier.padding(bottom = 32.dp))

            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), singleLine = true)

            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation())

            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation())

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                        if (password == confirmPassword) {
                            val usersJson = sharedPreferences.getString("users", "[]") ?: "[]"
                            val userList = gson.fromJson<List<User>>(usersJson, object : TypeToken<List<User>>() {}.type).toMutableList()

                            if (userList.any { it.username == username }) {
                                errorMessage = "El usuario ya existe"
                            } else {
                                userList.add(User(username, password))
                                sharedPreferences.edit().putString("users", gson.toJson(userList)).apply()

                                navController.navigate("login")
                            }
                        } else {
                            errorMessage = "Las contraseñas no coinciden"
                        }
                    } else {
                        errorMessage = "Por favor, completa todos los campos"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Registrar", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("login") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "¿Ya tienes una cuenta? Inicia sesión aquí", color = Color(0xFF6200EE), fontSize = 16.sp)
            }
        }
    }
}
