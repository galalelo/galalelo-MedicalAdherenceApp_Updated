package com.group4.medicaladherenceapp.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import kotlinx.coroutines.launch

class AboutAccountActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_account)
        lifecycleScope.launch {
            try {
                val user = repository.getProfile()
                findViewById<TextView>(R.id.txtEmail).text = user.email
                findViewById<TextView>(R.id.txtPhone).text = user.phone
                findViewById<TextView>(R.id.txtCondition).text = user.medicalCondition
            } catch (e: Exception) { Toast.makeText(this@AboutAccountActivity, e.localizedMessage ?: "Could not load account", Toast.LENGTH_SHORT).show() }
        }
    }
}
