package com.example.petconnectt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petconnectt.adapter.PetAdapter
import com.example.petconnectt.model.Pet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class AdoptPetsActivity : AppCompatActivity() {

    private lateinit var petRecyclerView: RecyclerView
    private lateinit var petAdapter: PetAdapter
    private val petList = mutableListOf<Pet>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var addPetFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopt_pets)

        petRecyclerView = findViewById(R.id.petRecyclerView)
        petRecyclerView.layoutManager = LinearLayoutManager(this)
        petAdapter = PetAdapter(petList)
        petRecyclerView.adapter = petAdapter


        addPetFab = findViewById(R.id.addPetFab)
        addPetFab.setOnClickListener {
            startActivity(Intent(this, AddPetActivity::class.java))
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("pets")
        fetchPetsFromFirebase()
    }

    private fun fetchPetsFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                petList.clear()
                for (petSnapshot in snapshot.children) {
                    val pet = petSnapshot.getValue(Pet::class.java)
                    pet?.let { petList.add(it) }
                }
                petAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AdoptPetsActivity", "Failed to fetch pets: ${error.message}")
                Toast.makeText(this@AdoptPetsActivity, "Failed to load pets.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
