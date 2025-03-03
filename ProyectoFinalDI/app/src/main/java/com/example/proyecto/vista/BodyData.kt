package com.example.proyecto.vista

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BodyData(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    var edad by remember { mutableStateOf(sharedPreferences.getInt("edad", 0).toString()) }
    var peso by remember { mutableStateOf(sharedPreferences.getFloat("peso", 0f).toString()) }
    var altura by remember { mutableStateOf(sharedPreferences.getFloat("altura", 0f).toString()) }
    var sexo by remember { mutableStateOf(sharedPreferences.getString("sexo", "") ?: "") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Datos del Usuario", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (cm)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = sexo,
            onValueChange = { sexo = it },
            label = { Text("Sexo") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                sharedPreferences.edit()
                    .putInt("edad", edad.toIntOrNull() ?: 0)
                    .putFloat("peso", peso.toFloatOrNull() ?: 0f)
                    .putFloat("altura", altura.toFloatOrNull() ?: 0f)
                    .putString("sexo", sexo)
                    .apply()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Guardar Datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a la pantalla principal")
        }
    }
}
