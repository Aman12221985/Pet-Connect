package com.example.petconnectt


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var otpEditText: EditText
    private lateinit var verifyButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        otpEditText = findViewById(R.id.editTextOtp)
        verifyButton = findViewById(R.id.buttonVerifyOtp)
        auth = FirebaseAuth.getInstance()

        verificationId = intent.getStringExtra("verificationId")!!

        verifyButton.setOnClickListener {
            val otp = otpEditText.text.toString()
            if (otp.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                signInWithCredential(credential)
            } else {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
