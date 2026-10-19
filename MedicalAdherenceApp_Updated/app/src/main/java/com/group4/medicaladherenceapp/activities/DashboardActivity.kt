package com.group4.medicaladherenceapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import com.group4.medicaladherenceapp.utils.SessionManager
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    private lateinit var txtMedicationCount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        txtMedicationCount = findViewById(R.id.txtMedicationCount)
        findViewById<LinearLayout>(R.id.btnMedications).setOnClickListener { startActivity(Intent(this, MedicationActivity::class.java)) }
        findViewById<LinearLayout>(R.id.btnNotifications).setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        findViewById<LinearLayout>(R.id.btnProfile).setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
        findViewById<LinearLayout>(R.id.btnAbout).setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        findViewById<Button>(R.id.btnDashboardLogout).setOnClickListener {
            repository.signOut()
            SessionManager(this).logout()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
    }
    override fun onResume() { super.onResume(); loadSummary() }
    private fun loadSummary() {
        lifecycleScope.launch {
            try {
                val medications = repository.getMedications()
                val (total, taken) = repository.getTodayAdherence()
                txtMedicationCount.text = "${medications.size} medications • $taken/$total doses taken today"
            } catch (e: Exception) { Toast.makeText(this@DashboardActivity, e.localizedMessage ?: "Could not refresh dashboard", Toast.LENGTH_SHORT).show() }
        }
    }
}
