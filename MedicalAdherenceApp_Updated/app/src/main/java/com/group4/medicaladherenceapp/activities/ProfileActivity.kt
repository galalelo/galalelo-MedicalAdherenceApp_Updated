package com.group4.medicaladherenceapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import com.group4.medicaladherenceapp.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        findViewById<Button>(R.id.btnEditProfile).setOnClickListener { startActivity(Intent(this, EditProfileActivity::class.java)) }
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            repository.signOut()
            SessionManager(this).logout()
            startActivity(Intent(this, LoginActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
            finish()
        }
    }
    override fun onResume() { super.onResume(); loadProfile() }
    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                val user = repository.getProfile()
                findViewById<TextView>(R.id.txtName).text = user.fullName
                findViewById<TextView>(R.id.txtEmail).text = user.email
                findViewById<TextView>(R.id.txtPhone).text = user.phone
                findViewById<TextView>(R.id.txtCondition).text = user.medicalCondition
            } catch (e: Exception) { Toast.makeText(this@ProfileActivity, e.localizedMessage ?: "Could not load profile", Toast.LENGTH_SHORT).show() }
        }
    }
}
