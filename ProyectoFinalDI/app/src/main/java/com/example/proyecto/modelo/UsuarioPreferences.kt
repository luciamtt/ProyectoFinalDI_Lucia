package com.example.proyecto.modelo



import android.content.Context
import android.content.SharedPreferences

class UsuarioPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("usuario_pref", Context.MODE_PRIVATE)

    // Guardar usuario y contraseña
    fun guardarUsuario(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    // Obtener nombre de usuario guardado
    fun obtenerUsuario(): String? {
        return sharedPreferences.getString("username", null)
    }

    // Obtener contraseña guardada
    fun obtenerContraseña(): String? {
        return sharedPreferences.getString("password", null)
    }

    // Verificar si el usuario está registrado
    fun estaUsuarioRegistrado(): Boolean {
        return sharedPreferences.contains("username") && sharedPreferences.contains("password")
    }

    // Limpiar los datos del usuario (cuando se cierra sesión)
    fun borrarDatosUsuario() {
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("password")
        editor.apply()
    }
}
