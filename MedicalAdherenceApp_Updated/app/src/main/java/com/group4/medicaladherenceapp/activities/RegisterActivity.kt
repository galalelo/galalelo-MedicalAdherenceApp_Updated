package com.group4.medicaladherenceapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val phone = findViewById<EditText>(R.id.etPhone)
        val condition = findViewById<EditText>(R.id.etCondition)
        val password = findViewById<EditText>(R.id.etPassword)
        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val fullName = name.text.toString().trim(); val userEmail = email.text.toString().trim()
            val userPhone = phone.text.toString().trim(); val userCondition = condition.text.toString().trim(); val pass = password.text.toString()
            if (fullName.isBlank() || userEmail.isBlank() || pass.isBlank()) { toast("Please fill in all required fields"); return@setOnClickListener }
            if (pass.length < 6) { toast("Password must be at least 6 characters"); return@setOnClickListener }
            lifecycleScope.launch {
                try {
                    repository.register(userEmail, pass, fullName, userPhone, userCondition)
                    toast("Account created")
                    finish()
                } catch (e: Exception) { toast(e.localizedMessage ?: "Could not create account") }
            }
        }
    }
    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
