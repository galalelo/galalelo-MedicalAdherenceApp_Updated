package com.group4.medicaladherenceapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.firebase.FirebaseRepository
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

class NotificationsActivity : AppCompatActivity() {
    private val repository = FirebaseRepository()
    private lateinit var container: LinearLayout
    private lateinit var empty: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        requestNotificationPermission()
        container = findViewById(R.id.notificationsContainer)
        empty = findViewById(R.id.txtNotificationsEmpty)
    }
    override fun onResume() { super.onResume(); loadNotifications() }
    private fun loadNotifications() {
        lifecycleScope.launch {
            try {
                val rows = repository.getNotifications()
                container.removeAllViews()
                empty.visibility = if (rows.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
                rows.asReversed().forEach { row ->
                    val card = LinearLayout(this@NotificationsActivity).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(36, 32, 36, 32)
                        setBackgroundColor(android.graphics.Color.WHITE)
                        val title = TextView(context).apply { text = row["title"]?.toString().orEmpty(); textSize = 18f; setTypeface(null, android.graphics.Typeface.BOLD); setTextColor(android.graphics.Color.BLACK) }
                        val message = TextView(context).apply { text = row["message"]?.toString().orEmpty(); textSize = 15f; setPadding(0, 8, 0, 0) }
                        val timestamp = (row["notificationDate"] as? Number)?.toLong()
                        val date = TextView(context).apply { text = timestamp?.let { DateFormat.getDateTimeInstance().format(Date(it)) }.orEmpty(); textSize = 12f; setPadding(0, 8, 0, 0) }
                        addView(title); addView(message); addView(date)
                    }
                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 16 }
                    card.gravity = Gravity.START
                    container.addView(card, params)
                }
            } catch (e: Exception) { Toast.makeText(this@NotificationsActivity, e.localizedMessage ?: "Could not load notifications", Toast.LENGTH_SHORT).show() }
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }
}
