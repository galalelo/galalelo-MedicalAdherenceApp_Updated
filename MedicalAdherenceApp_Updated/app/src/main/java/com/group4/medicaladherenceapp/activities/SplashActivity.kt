package com.group4.medicaladherenceapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.group4.medicaladherenceapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val destination = if (FirebaseAuth.getInstance().currentUser != null) DashboardActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, destination))
            finish()
        }, 700)
    }
}
