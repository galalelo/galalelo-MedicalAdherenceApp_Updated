package com.group4.medicaladherenceapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import com.group4.medicaladherenceapp.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()

    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST = 721
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField = findViewById<TextInputEditText>(R.id.etEmail)
        val passwordField = findViewById<TextInputEditText>(R.id.etPassword)
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            if (email.isBlank() || password.isBlank()) {
                toast("Please enter email and password")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                try {
                    repository.login(email, password)
                    SessionManager(this@LoginActivity).saveUserEmail(email)
                    openDashboard()
                } catch (e: Exception) {
                    toast(e.localizedMessage ?: "Could not sign in")
                }
            }
        }

        findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener { startGoogleSignIn() }
        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        findViewById<TextView>(R.id.txtForgotPassword).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun startGoogleSignIn() {
        try {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_web_client_id))
                .build()
            startActivityForResult(GoogleSignIn.getClient(this, options).signInIntent, GOOGLE_SIGN_IN_REQUEST)
        } catch (e: Exception) {
            toast(e.localizedMessage ?: "Google sign-in is not configured")
        }
    }

    @Deprecated("Google Sign-In returns its result through this activity callback")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != GOOGLE_SIGN_IN_REQUEST) return

        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)
            val token = account.idToken ?: error("Google did not return an ID token")
            val email = account.email.orEmpty()
            lifecycleScope.launch {
                try {
                    repository.loginWithGoogle(token, account.displayName.orEmpty(), email)
                    SessionManager(this@LoginActivity).saveUserEmail(email)
                    openDashboard()
                } catch (e: Exception) {
                    toast(e.localizedMessage ?: "Could not sign in with Google")
                }
            }
        } catch (e: ApiException) {
            if (e.statusCode != 12501) {
                toast(e.localizedMessage ?: "Google sign-in failed")
            }
        } catch (e: Exception) {
            toast(e.localizedMessage ?: "Google sign-in failed")
        }
    }

    private fun openDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
