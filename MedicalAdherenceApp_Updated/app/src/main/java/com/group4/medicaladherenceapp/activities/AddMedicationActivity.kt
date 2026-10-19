package com.group4.medicaladherenceapp.activities

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import com.group4.medicaladherenceapp.utils.AlarmScheduler
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class AddMedicationActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medication)
        val timeField = findViewById<EditText>(R.id.etTime)
        val nameField = findViewById<EditText>(R.id.etMedicationName)
        val dosageField = findViewById<EditText>(R.id.etDosage)
        timeField.setOnClickListener {
            val now = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute -> timeField.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute)) }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
        }
        findViewById<Button>(R.id.btnSaveMedication).setOnClickListener {
            val name = nameField.text.toString().trim(); val dosage = dosageField.text.toString().trim(); val time = timeField.text.toString().trim()
            if (name.isBlank() || dosage.isBlank() || time.isBlank()) { toast("Complete all fields"); return@setOnClickListener }
            lifecycleScope.launch {
                try {
                    repository.addMedication(name, dosage, time)
                    val parts = time.split(":")
                    if (parts.size == 2) AlarmScheduler(this@AddMedicationActivity).scheduleMedicationReminder(name, parts[0].toInt(), parts[1].toInt())
                    toast("Medication saved to your account")
                    finish()
                } catch (e: Exception) { toast(e.localizedMessage ?: "Could not save medication") }
            }
        }
    }
    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
