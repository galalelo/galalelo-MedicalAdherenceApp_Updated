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

class EditMedicationActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medication)
        val id = intent.getStringExtra("MEDICATION_ID") ?: run { finish(); return }
        val name = findViewById<EditText>(R.id.etMedicationName)
        val dosage = findViewById<EditText>(R.id.etDosage)
        val time = findViewById<EditText>(R.id.etTime)
        lifecycleScope.launch {
            try { repository.getMedication(id)?.let { name.setText(it.name); dosage.setText(it.dosage); time.setText(it.time) } ?: finish() }
            catch (e: Exception) { Toast.makeText(this@EditMedicationActivity, e.localizedMessage ?: "Could not load medication", Toast.LENGTH_SHORT).show() }
        }
        findViewById<Button>(R.id.btnUpdateMedication).setOnClickListener {
            if (name.text.isNullOrBlank() || dosage.text.isNullOrBlank() || time.text.isNullOrBlank()) { Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            lifecycleScope.launch {
                try { repository.updateMedication(id, name.text.toString().trim(), dosage.text.toString().trim(), time.text.toString().trim()); Toast.makeText(this@EditMedicationActivity, "Medication updated", Toast.LENGTH_SHORT).show(); finish() }
                catch (e: Exception) { Toast.makeText(this@EditMedicationActivity, e.localizedMessage ?: "Could not update medication", Toast.LENGTH_SHORT).show() }
            }
        }
    }
}
