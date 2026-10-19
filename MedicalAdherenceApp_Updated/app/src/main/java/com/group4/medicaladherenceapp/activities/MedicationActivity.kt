package com.group4.medicaladherenceapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.adapters.MedicationAdapter
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import kotlinx.coroutines.launch

class MedicationActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medication)
        findViewById<Button>(R.id.btnAddMedication).setOnClickListener { startActivity(Intent(this, AddMedicationActivity::class.java)) }
        recyclerView = findViewById(R.id.recyclerViewMedications)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() { super.onResume(); loadMedications() }
    private fun loadMedications() {
        lifecycleScope.launch {
            try {
                val list = repository.getMedications()
                recyclerView.adapter = MedicationAdapter(list,
                    { medication -> startActivity(Intent(this@MedicationActivity, EditMedicationActivity::class.java).putExtra("MEDICATION_ID", medication.id)) },
                    { medication -> lifecycleScope.launch { try { repository.deleteMedication(medication.id); Toast.makeText(this@MedicationActivity, "Medication deleted", Toast.LENGTH_SHORT).show(); loadMedications() } catch (e: Exception) { Toast.makeText(this@MedicationActivity, e.localizedMessage ?: "Could not delete medication", Toast.LENGTH_SHORT).show() } } },
                    { medication -> startActivity(Intent(this@MedicationActivity, ConfirmMedicationActivity::class.java).putExtra("MEDICATION_ID", medication.id).putExtra("MEDICATION_NAME", medication.name)) })
            } catch (e: Exception) { Toast.makeText(this@MedicationActivity, e.localizedMessage ?: "Could not load medications", Toast.LENGTH_SHORT).show() }
        }
    }
}
