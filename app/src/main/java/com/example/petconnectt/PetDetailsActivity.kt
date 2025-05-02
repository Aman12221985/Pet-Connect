package com.example.petconnectt

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.petconnectt.R

class PetDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)

        val petType = intent.getStringExtra("petType") ?: "Unknown"
        val breedName = intent.getStringExtra("breedName") ?: "Unknown Breed"
        val age = intent.getStringExtra("age") ?: "0"
        val location = intent.getStringExtra("location") ?: "Unknown Location"
        val price = intent.getStringExtra("price") ?: "N/A"
        val ownerContact = intent.getStringExtra("ownerContact") ?: "N/A"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        val petImage = findViewById<ImageView>(R.id.petImageDetail)
        val petName = findViewById<TextView>(R.id.petNameDetail)
        val petBreed = findViewById<TextView>(R.id.petBreedDetail)
        val petAge = findViewById<TextView>(R.id.petAgeDetail)
        val petLocation = findViewById<TextView>(R.id.petLocationDetail)
        val petPrice = findViewById<TextView>(R.id.petPriceDetail)
        val petOwnerContact = findViewById<TextView>(R.id.petOwnerContactDetail)
        val interestedButton = findViewById<Button>(R.id.btnInterested)

        Glide.with(this)
            .load(imageUrl.ifEmpty { R.drawable.ic_pets })
            .into(petImage)

        petName.text = "Type: $petType"
        petBreed.text = "Breed: $breedName"
        petAge.text = "Age: $age months"
        petLocation.text = "Location: $location"
        petPrice.text = "Price: ₹$price"
        petOwnerContact.text = "Contact: $ownerContact"

        interestedButton.setOnClickListener {
            showInterestDialog(ownerContact)
        }
    }

    private fun showInterestDialog(ownerContact: String) {
        AlertDialog.Builder(this)
            .setTitle("Interest Marked!")
            .setMessage("You have shown interest in adopting this pet.\n\nPlease contact the owner:\n$ownerContact")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
