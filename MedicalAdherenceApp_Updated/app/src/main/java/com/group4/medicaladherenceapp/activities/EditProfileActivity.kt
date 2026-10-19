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

class EditProfileActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val phone = findViewById<EditText>(R.id.etPhone)
        val condition = findViewById<EditText>(R.id.etCondition)
        email.isEnabled = false
        lifecycleScope.launch {
            try { val profile = repository.getProfile(); name.setText(profile.fullName); email.setText(profile.email); phone.setText(profile.phone); condition.setText(profile.medicalCondition) }
            catch (e: Exception) { Toast.makeText(this@EditProfileActivity, e.localizedMessage ?: "Could not load profile", Toast.LENGTH_SHORT).show() }
        }
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            if (name.text.isNullOrBlank()) { Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            lifecycleScope.launch {
                try { repository.updateProfile(name.text.toString().trim(), phone.text.toString().trim(), condition.text.toString().trim()); Toast.makeText(this@EditProfileActivity, "Profile updated", Toast.LENGTH_SHORT).show(); finish() }
                catch (e: Exception) { Toast.makeText(this@EditProfileActivity, e.localizedMessage ?: "Could not update profile", Toast.LENGTH_SHORT).show() }
            }
        }
    }
}
