package com.example.petconnectt.model

data class Pet(
    val id: String = "",
    val petType: String = "",
    val breedName: String = "",
    val age: String = "",
    val location: String = "",
    val price: String = "",
    val ownerContact: String = "",
    val imageUrl: String = "",
    var interested: Boolean = false
)
