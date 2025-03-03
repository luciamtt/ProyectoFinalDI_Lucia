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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BodyDataScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) }

    var age by remember { mutableStateOf(sharedPreferences.getInt("age", 0).toString()) }
    var weight by remember { mutableStateOf(sharedPreferences.getFloat("weight", 0f).toString()) }
    var height by remember { mutableStateOf(sharedPreferences.getFloat("height", 0f).toString()) }
    var gender by remember { mutableStateOf(sharedPreferences.getString("gender", "") ?: "") }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Datos Personales", fontSize = 32.sp, color = Color(0xFF6200EE), modifier = Modifier.padding(bottom = 32.dp))

            OutlinedTextField(value = age,
                onValueChange = { age = it },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next))

            OutlinedTextField(value = weight,
                onValueChange = { weight = it },
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next))

            OutlinedTextField(value = height,
                onValueChange = { height = it },
                label = { Text("Altura (cm)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next))

            OutlinedTextField(value = gender,
                onValueChange = { gender = it },
                label = { Text("Género (M/F)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Guardamos los datos en las preferencias compartidas
                    sharedPreferences.edit()
                        .putInt("age", age.toIntOrNull() ?: 0)
                        .putFloat("weight", weight.toFloatOrNull() ?: 0f)
                        .putFloat("height", height.toFloatOrNull() ?: 0f)
                        .putString("gender", gender)
                        .apply()

                    // Navegamos a la pantalla de lista de pruebas
                    navController.navigate("test_list")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Guardar Datos", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}


