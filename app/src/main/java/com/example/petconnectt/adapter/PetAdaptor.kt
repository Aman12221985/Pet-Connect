package com.example.petconnectt.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petconnectt.PetDetailsActivity
import com.example.petconnectt.R
import com.example.petconnectt.model.Pet
import android.content.Intent
import com.google.firebase.database.FirebaseDatabase

class PetAdapter(
    private val pets: List<Pet>
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petImage: ImageView = itemView.findViewById(R.id.petImage)
        val petName: TextView = itemView.findViewById(R.id.petName)
        val petBreed: TextView = itemView.findViewById(R.id.petBreed)
        val petAge: TextView = itemView.findViewById(R.id.petAge)
        val menuOptions: ImageView = itemView.findViewById(R.id.menuOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]

        Glide.with(holder.itemView.context)
            .load(pet.imageUrl.ifEmpty { R.drawable.ic_pets })
            .into(holder.petImage)

        holder.petName.text = pet.petType
        holder.petBreed.text = pet.breedName
        holder.petAge.text = "Age: ${pet.age} months"

        // Navigate to Pet Details
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PetDetailsActivity::class.java).apply {
                putExtra("id", pet.id)
                putExtra("petType", pet.petType)
                putExtra("breedName", pet.breedName)
                putExtra("age", pet.age)
                putExtra("location", pet.location)
                putExtra("price", pet.price)
                putExtra("ownerContact", pet.ownerContact)
                putExtra("imageUrl", pet.imageUrl)
            }
            context.startActivity(intent)
        }

        // Handle 3-dot menu click
        holder.menuOptions.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.pet_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        showEditDialog(view, pet)
                        true
                    }
                    R.id.action_delete -> {
                        showDeleteConfirmation(view, pet)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int = pets.size

    private fun showEditDialog(view: View, pet: Pet) {
        val context = view.context
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_pet, null)

        val editPetType = dialogView.findViewById<EditText>(R.id.editPetType)
        val editBreedName = dialogView.findViewById<EditText>(R.id.editBreedName)
        val editAge = dialogView.findViewById<EditText>(R.id.editAge)
        val editLocation = dialogView.findViewById<EditText>(R.id.editLocation)
        val editPrice = dialogView.findViewById<EditText>(R.id.editPrice)
        val editOwnerContact = dialogView.findViewById<EditText>(R.id.editOwnerContact)

        // Pre-fill existing data
        editPetType.setText(pet.petType)
        editBreedName.setText(pet.breedName)
        editAge.setText(pet.age)
        editLocation.setText(pet.location)
        editPrice.setText(pet.price)
        editOwnerContact.setText(pet.ownerContact)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Edit Pet Details")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedPet = mapOf(
                    "petType" to editPetType.text.toString(),
                    "breedName" to editBreedName.text.toString(),
                    "age" to editAge.text.toString(),
                    "location" to editLocation.text.toString(),
                    "price" to editPrice.text.toString(),
                    "ownerContact" to editOwnerContact.text.toString()
                )

                val database = FirebaseDatabase.getInstance().getReference("pets")
                database.child(pet.id).updateChildren(updatedPet)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirmation(view: View, pet: Pet) {
        val context = view.context
        val dialog = AlertDialog.Builder(context)
            .setTitle("Delete Pet")
            .setMessage("Are you sure you want to delete this pet?")
            .setPositiveButton("Yes") { _, _ ->
                val database = FirebaseDatabase.getInstance().getReference("pets")
                database.child(pet.id).removeValue()
            }
            .setNegativeButton("No", null)
            .create()

        dialog.show()
    }
}
