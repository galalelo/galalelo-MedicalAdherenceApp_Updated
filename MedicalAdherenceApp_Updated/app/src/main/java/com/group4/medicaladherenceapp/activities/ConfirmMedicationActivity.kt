package com.group4.medicaladherenceapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import kotlinx.coroutines.launch

class ConfirmMedicationActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_medication)
        val medicationId = intent.getStringExtra("MEDICATION_ID")
        val medicationName = intent.getStringExtra("MEDICATION_NAME").orEmpty()
        if (medicationId.isNullOrBlank()) { Toast.makeText(this, "Medication not found", Toast.LENGTH_SHORT).show(); finish(); return }
        findViewById<TextView>(R.id.txtDosePrompt).text = "Did you take $medicationName?"
        findViewById<Button>(R.id.btnDoseTaken).setOnClickListener { saveDose(medicationId, "Taken") }
        findViewById<Button>(R.id.btnDoseMissed).setOnClickListener { saveDose(medicationId, "Missed") }
    }
    private fun saveDose(id: String, status: String) {
        lifecycleScope.launch {
            try { repository.logDose(id, status); Toast.makeText(this@ConfirmMedicationActivity, "Dose recorded", Toast.LENGTH_SHORT).show(); finish() }
            catch (e: Exception) { Toast.makeText(this@ConfirmMedicationActivity, e.localizedMessage ?: "Could not record dose", Toast.LENGTH_SHORT).show() }
        }
    }
}
