package com.example.proyecto.vista

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto.modelo.UserData
import com.example.proyecto.modelo.UserDataPreference

@Composable
fun BodyData(navController: NavController) {
    val context = LocalContext.current // Obtén el contexto correctamente
    val userPreferences = remember { UserDataPreference(context) }
    var userData by remember { mutableStateOf(userPreferences.getUserData() ?: UserData(0, 0f, 0f, "")) }

    var edad by remember { mutableStateOf(userData.edad.toString()) }
    var peso by remember { mutableStateOf(userData.peso.toString()) }
    var altura by remember { mutableStateOf(userData.altura.toString()) }
    var sexo by remember { mutableStateOf(userData.sexo) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Datos del Usuario", style = MaterialTheme.typography.headlineMedium)

        // Campos de entrada para edad, peso, altura, sexo
        OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = altura, onValueChange = { altura = it }, label = { Text("Altura (cm)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = sexo, onValueChange = { sexo = it }, label = { Text("Sexo") },
            modifier = Modifier.fillMaxWidth())

        // Botón para guardar los datos
        Button(onClick = {
            val newUserData = UserData(
                edad.toIntOrNull() ?: 0,
                peso.toFloatOrNull() ?: 0f,
                altura.toFloatOrNull() ?: 0f,
                sexo
            )
            userPreferences.saveUserData(newUserData)  // Guardar los datos en SharedPreferences
            userData = newUserData

            // Navegar a otra pantalla, por ejemplo, de vuelta al login o dashboard
            navController.navigate("someOtherScreen")  // Reemplaza "someOtherScreen" con la ruta de la pantalla a la que deseas navegar
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Guardar Datos")
        }
    }
}

