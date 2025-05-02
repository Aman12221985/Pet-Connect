package com.example.petconnectt

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petconnectt.model.Pet
import com.google.firebase.database.FirebaseDatabase

class AddPetActivity : AppCompatActivity() {

    private lateinit var petTypeEditText: EditText
    private lateinit var breedNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var ownerContactEditText: EditText
    private lateinit var addPetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        petTypeEditText = findViewById(R.id.petTypeEditText)
        breedNameEditText = findViewById(R.id.breedNameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        locationEditText = findViewById(R.id.locationEditText)
        priceEditText = findViewById(R.id.priceEditText)
        ownerContactEditText = findViewById(R.id.ownerContactEditText)
        addPetButton = findViewById(R.id.addPetButton)

        addPetButton.setOnClickListener {
            addPetToDatabase()
        }
    }

    private fun addPetToDatabase() {
        val petType = petTypeEditText.text.toString().trim()
        val breedName = breedNameEditText.text.toString().trim()
        val age = ageEditText.text.toString().trim()
        val location = locationEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim()
        val ownerContact = ownerContactEditText.text.toString().trim()

        if (petType.isEmpty() || breedName.isEmpty() || age.isEmpty() || location.isEmpty() || price.isEmpty() || ownerContact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance()
        val petsRef = database.getReference("pets")
        val petId = petsRef.push().key.orEmpty()

        val pet = Pet(
            id = petId,
            petType = petType,
            breedName = breedName,
            age = age,
            location = location,
            price = price,
            ownerContact = ownerContact,
            imageUrl = "" // You can handle uploading image separately later
        )

        petsRef.child(petId).setValue(pet)
            .addOnSuccessListener {
                Toast.makeText(this, "Pet Added Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to Add Pet: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
