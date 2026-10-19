package com.group4.medicaladherenceapp.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class SessionManager(context: Context) {
    private val preferences = context.getSharedPreferences("MedicalAdherenceSession", Context.MODE_PRIVATE)
    private val auth = FirebaseAuth.getInstance()

    fun saveUserEmail(email: String) {
        preferences.edit().putString("USER_EMAIL", email).apply()
    }

    fun getUserEmail(): String? = auth.currentUser?.email ?: preferences.getString("USER_EMAIL", null)

    fun logout() {
        auth.signOut()
        preferences.edit().clear().apply()
    }
}
