package com.example.proyecto.vista

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto.R


// Modelo de prueba física
data class PhysicalTest(
    val name: String,
    val result: String,
    val imageResId: Int,
    val detailUrl: String,
    val description: String,
    val category: String
)

// Lista de pruebas físicas (de ejemplo)
val testList = listOf(
    PhysicalTest("Abdominales 30\"", "21", R.drawable.abdominales, "https://es.wikipedia.org/wiki/Abdominales", "Prueba de abdominales en 30 segundos.", "Fuerza muscular"),
    PhysicalTest("Flexibilidad", "-7", R.drawable.flexibilidad, "https://es.wikipedia.org/wiki/Flexibilidad_f%C3%ADsica", "Medición de la flexibilidad en centímetros.", "Flexibilidad"),
    PhysicalTest("Test Cooper", "7 Periodos", R.drawable.course_navette, "https://es.wikipedia.org/wiki/Test_de_Cooper", "Prueba de resistencia cardiovascular.", "Resistencia"),
    PhysicalTest("Salto Horizontal", "2m", R.drawable.salto_horizontal, "https://es.wikipedia.org/wiki/Salto_de_longitud", "Prueba de salto horizontal.", "Agilidad"),
    PhysicalTest("Sprint 5x10", "9.5 segundos", R.drawable.spirng, "https://es.wikipedia.org/wiki/Sprint", "Prueba de velocidad.", "Velocidad"),
    PhysicalTest("Prueba de coordinación", "Excelente", R.drawable.balon, "https://es.wikipedia.org/wiki/Coordinaci%C3%B3n", "Evaluación de la coordinación motora.", "Coordinación")
)



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestListScreen(navController: NavController) {
    val context = LocalContext.current

    // Estado para manejar la búsqueda
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Agrupar las pruebas por categoría
    val groupedTests = testList.groupBy { it.category }

    // Filtrar las pruebas en base a la búsqueda
    val filteredGroupedTests = groupedTests.mapValues { (_, tests) ->
        tests.filter { test -> test.name.contains(searchQuery.text, ignoreCase = true) }
    }

    // Composición de la pantalla
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Barra de búsqueda
        SearchView(searchQuery, onSearchQueryChange = { searchQuery = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar a la pantalla del IMC
        Button(onClick = { navController.navigate("imc_screen") }) {
            Text(text = "Ver IMC")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar las pruebas agrupadas por categoría con Sticky Header
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            filteredGroupedTests.forEach { (category, tests) ->
                stickyHeader {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .padding(8.dp)
                    )
                }

                items(tests) { test ->
                    TestCard(test = test, navController = navController, context = context)
                }
            }
        }
    }
}
@Composable
fun SearchView(searchQuery: TextFieldValue, onSearchQueryChange: (TextFieldValue) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = { newQuery -> onSearchQueryChange(newQuery) },
        label = { Text("Buscar prueba") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp)
    )
}
@Composable
fun TestDetailScreen(navController: NavController, testName: String) {
    // Buscamos la prueba con el nombre recibido por la navegación
    val test = testList.find { it.name == testName }

    // Si no se encuentra, retornamos (aunque esto no debería suceder si todo está bien)
    if (test == null) {
        Text(text = "Prueba no encontrada", color = Color.Red)
        return
    }

    // Composición para mostrar los detalles de la prueba
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Título con el nombre de la prueba
        Text(
            text = test.name,
            style = TextStyle(fontSize = 24.sp, color = Color.Black),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Imagen de la prueba
        Image(
            painter = painterResource(id = test.imageResId),
            contentDescription = test.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        // Resultado de la prueba
        Text(
            text = "Resultado: ${test.result}",
            style = TextStyle(fontSize = 20.sp, color = Color.Black),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Descripción de la prueba
        Text(
            text = "Descripción: ${test.description}",
            style = TextStyle(fontSize = 16.sp, color = Color.Gray),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botón para abrir el enlace de la prueba en el navegador
        Button(
            onClick = { openUrlInBrowser(test.detailUrl, navController.context) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Ver detalles de la prueba")
        }
    }
}

@Composable
fun TestCard(test: PhysicalTest, navController: NavController, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { openUrlInBrowser(test.detailUrl, context) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Imagen de la prueba utilizando painterResource para cargar una imagen local
            Image(
                painter = painterResource(id = test.imageResId),
                contentDescription = test.name,
                modifier = Modifier.size(100.dp).padding(end = 16.dp)
            )
            // Información de la prueba
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = test.name,
                    style = TextStyle(fontSize = 20.sp, color = Color(0xFF6200EE))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Resultado: ${test.result}",
                    style = TextStyle(fontSize = 16.sp, color = Color(0xFF333333))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = test.description,
                    style = TextStyle(fontSize = 14.sp, color = Color(0xFF333333)),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = {
                    openUrlInBrowser(test.detailUrl, context)
                }) {
                    Text(text = "Explicar prueba", color = Color(0xFF6200EE))
                }
            }
        }
    }
}

// Función para abrir el enlace en el navegador
fun openUrlInBrowser(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}


fun calcularNota(abdominales: Int, flexibilidad: Int, testCooper: Int, velocidad: Int): Double {
    // Puntaje base para cada test. Vamos a utilizar un sistema simple de asignación de puntos:

    // Abdominales: Supongamos que por cada abdominal realizado se otorgan 0.5 puntos, con un máximo de 30 abdominales.
    val abdominalesScore = if (abdominales in 0..30) abdominales * 0.5 else 15.0  // Maximo 15 puntos

    // Flexibilidad: Vamos a asumir que por cada cm de flexibilidad se da un puntaje, hasta un máximo de -20cm a 20cm (donde 0 es el objetivo).
    val flexibilidadScore = when {
        flexibilidad >= 20 -> 10.0  // Muy flexible, puntaje máximo.
        flexibilidad >= 10 -> 8.0   // Bueno.
        flexibilidad >= 0 -> 6.0    // Promedio.
        flexibilidad >= -10 -> 4.0  // Bajo.
        flexibilidad >= -20 -> 2.0  // Muy bajo.
        else -> 0.0                 // No llegas a tocar los pies, puntaje mínimo.
    }

    // Test Cooper: El puntaje depende del número de períodos alcanzados.
    val cooperScore = when {
        testCooper >= 12 -> 10.0  // Muy bueno, más de 12 períodos.
        testCooper >= 9  -> 8.0   // Bueno, entre 9 y 11 períodos.
        testCooper >= 6  -> 6.0   // Promedio, entre 6 y 8 períodos.
        testCooper >= 3  -> 4.0   // Bajo, entre 3 y 5 períodos.
        else -> 2.0                // Muy bajo, menos de 3 períodos.
    }

    // Velocidad 5x10: Dependiendo del tiempo empleado en segundos.
    val velocidadScore = when {
        velocidad <= 30 -> 10.0  // Muy rápido, menos de 30 segundos.
        velocidad <= 35 -> 8.0   // Bueno, entre 30-35 segundos.
        velocidad <= 40 -> 6.0   // Promedio, entre 36-40 segundos.
        velocidad <= 45 -> 4.0   // Bajo, entre 41-45 segundos.
        else -> 2.0              // Muy bajo, más de 45 segundos.
    }

    // Calculando la nota total combinando los puntajes obtenidos
    val notaFinal = (abdominalesScore + flexibilidadScore + cooperScore + velocidadScore) / 4

    return notaFinal
}

@Composable
fun ImcScreen(navController: NavController) {
    // Estados para almacenar el peso y la altura
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var imcResult by remember { mutableStateOf(0.0) }

    // Calcular IMC
    fun calculateImc(weight: String, height: String): Double {
        val weightValue = weight.toDoubleOrNull()
        val heightValue = height.toDoubleOrNull()

        if (weightValue != null && heightValue != null && heightValue > 0) {
            return weightValue / (heightValue * heightValue)
        }

        return 0.0
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Calcular IMC", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el peso
        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para la altura
        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para calcular el IMC
        Button(onClick = {
            imcResult = calculateImc(weight, height)
        }) {
            Text(text = "Calcular IMC")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el IMC calculado
        if (imcResult > 0) {
            Text(text = "Tu IMC es: %.2f".format(imcResult), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

