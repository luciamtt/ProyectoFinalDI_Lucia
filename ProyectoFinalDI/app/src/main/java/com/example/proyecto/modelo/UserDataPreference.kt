package com.example.proyecto.modelo

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson

data class UserData(
    val edad: Int,
    val peso: Float,
    val altura: Float,
    val sexo: String
)

class UserDataPreference(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun saveUserData(userData: UserData) {
        val json = gson.toJson(userData)
        prefs.edit().putString("user_data", json).apply()
    }

    fun getUserData(): UserData? {
        val json = prefs.getString("user_data", null)
        return json?.let {
            gson.fromJson(it, UserData::class.java)
        }
    }
}
