package com.example.petconnectt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var phoneText: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        nameText = findViewById(R.id.profileName)
        emailText = findViewById(R.id.profileEmail)
        phoneText = findViewById(R.id.profilePhone)
        btnLogout = findViewById(R.id.btnLogout)
        btnEdit = findViewById(R.id.btnEditProfile)

        // Load user data
        val user = auth.currentUser
        user?.let {
            emailText.text = it.email
            phoneText.text = it.phoneNumber ?: "Phone not available"
            nameText.text = it.displayName ?: "Your Name"
        }

        // Logout button
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()  // Close the profile activity
        }

        // Edit profile button
        btnEdit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        val user = FirebaseAuth.getInstance().currentUser
        nameText.text = user?.displayName ?: "No name"

    }

}
