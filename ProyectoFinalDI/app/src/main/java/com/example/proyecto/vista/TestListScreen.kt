package com.example.proyecto.vista

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
fun TestEvaluationScreen(navController: NavController, testName: String) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("test_scores", Context.MODE_PRIVATE)

    var input1 by remember { mutableStateOf(prefs.getInt("$testName-input1", 0).toString()) }
    var input2 by remember { mutableStateOf(prefs.getInt("$testName-input2", 0).toString()) }
    var result by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Evaluación de $testName", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = input1,
            onValueChange = { input1 = it },
            label = { Text("Dato 1") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = input2,
            onValueChange = { input2 = it },
            label = { Text("Dato 2") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val score = calcularNota(input1.toIntOrNull() ?: 0, input2.toIntOrNull() ?: 0)
            result = "Nota Calculada: $score"
            saveScore(prefs, testName, input1.toIntOrNull() ?: 0, input2.toIntOrNull() ?: 0)
        }) {
            Text("Calcular Nota")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result, style = MaterialTheme.typography.bodyLarge)
    }
}

fun calcularNota(valor1: Int, valor2: Int): Double {
    return (valor1 + valor2) / 2.0
}

fun saveScore(prefs: SharedPreferences, testName: String, input1: Int, input2: Int) {
    prefs.edit()
        .putInt("$testName-input1", input1)
        .putInt("$testName-input2", input2)
        .apply()
}

@Composable
fun TestCard(test: PhysicalTest, navController: NavController, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable {
                navController.navigate("test_evaluation/${test.name}") // 🔹 Ahora lleva a la pantalla de evaluación
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = test.imageResId),
                contentDescription = test.name,
                modifier = Modifier.size(100.dp).padding(end = 16.dp)
            )
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

                TextButton(onClick = { openUrlInBrowser(test.detailUrl, context) }) {
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

    val abdominalesScore = if (abdominales in 0..30) abdominales * 0.5 else 15.0

    val flexibilidadScore = when {
        flexibilidad >= 20 -> 10.0
        flexibilidad >= 10 -> 8.0
        flexibilidad >= 0 -> 6.0
        flexibilidad >= -10 -> 4.0
        flexibilidad >= -20 -> 2.0
        else -> 0.0
    }

    val cooperScore = when {
        testCooper >= 12 -> 10.0
        testCooper >= 9  -> 8.0
        testCooper >= 6  -> 6.0
        testCooper >= 3  -> 4.0
        else -> 2.0
    }

    val velocidadScore = when {
        velocidad <= 30 -> 10.0
        velocidad <= 35 -> 8.0
        velocidad <= 40 -> 6.0
        velocidad <= 45 -> 4.0
        else -> 2.0
    }

    val notaFinal = (abdominalesScore + flexibilidadScore + cooperScore + velocidadScore) / 4

    return notaFinal
}

@Composable
fun IMCScreen(navController: NavController) {
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var imcResultado by remember { mutableStateOf<Double?>(null) }
    var categoriaIMC by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Calculadora de IMC", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val pesoKg = peso.toDoubleOrNull()
            val alturaM = altura.toDoubleOrNull()

            if (pesoKg != null && alturaM != null && alturaM > 0) {
                val imc = calcularIMC(pesoKg, alturaM)
                imcResultado = imc
                categoriaIMC = obtenerCategoriaIMC(imc)
            } else {
                imcResultado = null
                categoriaIMC = "Ingrese valores válidos"
            }
        }) {
            Text("Calcular IMC")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imcResultado?.let {
            Text("Tu IMC es: ${"%.2f".format(it)}", fontSize = 20.sp)
            Text("Categoría: $categoriaIMC", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// Función para calcular la categoría del IMC
fun obtenerCategoriaIMC(imc: Double): String {
    return when {
        imc < 18.5 -> "Bajo peso"
        imc < 24.9 -> "Peso normal"
        imc < 29.9 -> "Sobrepeso"
        else -> "Obesidad"
    }

}
fun calcularIMC(peso: Double, altura: Double): Double {
    return if (altura > 0) peso / (altura * altura) else 0.0
}


