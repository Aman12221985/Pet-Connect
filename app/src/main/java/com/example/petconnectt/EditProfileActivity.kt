package com.example.petconnectt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var changePhotoButton: Button
    private var imageUri: Uri? = null

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImageView = findViewById(R.id.editProfileImage)
        nameEditText = findViewById(R.id.editName)
        saveButton = findViewById(R.id.btnSaveProfile)
        changePhotoButton = findViewById(R.id.btnChangePhoto)

        nameEditText.setText(user?.displayName ?: "")

        changePhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString().trim()

            if (newName.isNotEmpty()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .setPhotoUri(imageUri)
                    .build()

                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // ✅ Reload the user so changes reflect immediately
                            user.reload().addOnCompleteListener {
                                Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()

                                // ✅ Pass updated name back to MainActivity
                                val resultIntent = Intent()
                                resultIntent.putExtra("updatedName", newName)
                                setResult(Activity.RESULT_OK, resultIntent)

                                finish()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Update failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            profileImageView.setImageURI(imageUri)
        }
    }
}
