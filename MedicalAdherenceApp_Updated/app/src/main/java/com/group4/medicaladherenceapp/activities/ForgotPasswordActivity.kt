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

class ForgotPasswordActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val emailField = findViewById<EditText>(R.id.etRecoveryEmail)
        findViewById<Button>(R.id.btnResetPassword).setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isBlank()) { Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            lifecycleScope.launch {
                try {
                    repository.sendPasswordReset(email)
                    Toast.makeText(this@ForgotPasswordActivity, "Password reset email sent", Toast.LENGTH_LONG).show()
                    finish()
                } catch (e: Exception) { Toast.makeText(this@ForgotPasswordActivity, e.localizedMessage ?: "Could not send reset email", Toast.LENGTH_LONG).show() }
            }
        }
    }
}
