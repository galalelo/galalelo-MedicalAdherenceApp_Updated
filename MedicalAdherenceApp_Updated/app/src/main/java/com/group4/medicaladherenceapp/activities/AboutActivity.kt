package com.group4.medicaladherenceapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.group4.medicaladherenceapp.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        val cardAboutAccount =
            findViewById<CardView>(
                R.id.cardAboutAccount
            )

        val cardPrivacy =
            findViewById<CardView>(
                R.id.cardPrivacy
            )

        val cardTerms =
            findViewById<CardView>(
                R.id.cardTerms
            )

        val cardAppInfo =
            findViewById<CardView>(
                R.id.cardAppInfo
            )

        val cardTeam =
            findViewById<CardView>(
                R.id.cardTeam
            )

        cardAboutAccount.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AboutAccountActivity::class.java
                )
            )
        }

        cardPrivacy.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PrivacyPolicyActivity::class.java
                )
            )
        }

        cardTerms.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    TermsOfUseActivity::class.java
                )
            )
        }

        cardAppInfo.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AppInfoActivity::class.java
                )
            )
        }

        cardTeam.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DeveloperInfoActivity::class.java
                )
            )
        }
    }
}