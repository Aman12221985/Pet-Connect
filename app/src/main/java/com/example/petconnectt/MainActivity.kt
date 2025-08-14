package com.example.petconnectt

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class MainActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView

    companion object {
        const val EDIT_PROFILE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Display welcome message with display name
        val user = FirebaseAuth.getInstance().currentUser
        welcomeText = findViewById(R.id.txtWelcome) // Make sure ID matches your XML
        welcomeText.text = "Welcome ${user?.displayName ?: ""}"

        // Setting up the cards for different activities
        findViewById<CardView>(R.id.cardAdopt).setOnClickListener {
            startActivity(Intent(this, AdoptPetsActivity::class.java))
        }

        findViewById<CardView>(R.id.cardTips).setOnClickListener {
            startActivity(Intent(this, PetCareTipsActivity::class.java))
        }

        findViewById<CardView>(R.id.cardApplications).setOnClickListener {
            startActivity(Intent(this, MyApplicationsActivity::class.java))
        }

        findViewById<CardView>(R.id.cardShelters).setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        findViewById<CardView>(R.id.cardVaccination).setOnClickListener {
            startActivity(Intent(this, VaccinationActivity::class.java))
        }

        findViewById<CardView>(R.id.cardMedicalRecords).setOnClickListener {
            startActivity(Intent(this, MedicalRecordsActivity::class.java))
        }

        findViewById<ImageView>(R.id.imgProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<CardView>(R.id.cardChat).setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // 🔄 Refresh name when activity resumes (good fallback in case of back navigation)
        val user = FirebaseAuth.getInstance().currentUser
        welcomeText.text = "Welcome ${user?.displayName ?: ""}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            val updatedName = data?.getStringExtra("updatedName")
            if (!updatedName.isNullOrEmpty()) {
                welcomeText.text = "Welcome $updatedName"
            }
        }
    }
}
