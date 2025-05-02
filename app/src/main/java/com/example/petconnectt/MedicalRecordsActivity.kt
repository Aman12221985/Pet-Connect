package com.example.petconnectt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petconnectt.adapter.MedicalRecordAdapter
import com.example.petconnectt.model.MedicalRecord
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class MedicalRecordsActivity : AppCompatActivity() {

    private lateinit var uploadBtn: Button
    private lateinit var recordsRecycler: RecyclerView
    private val recordList = mutableListOf<MedicalRecord>()
    private val storage = FirebaseStorage.getInstance().reference
    private val database = FirebaseDatabase.getInstance().getReference("medicalRecords")

    private val FILE_PICKER_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_records)

        uploadBtn = findViewById(R.id.btnUploadRecord)
        recordsRecycler = findViewById(R.id.recordsRecyclerView)
        recordsRecycler.layoutManager = LinearLayoutManager(this)

        uploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, FILE_PICKER_REQUEST)
        }

        loadRecords()
    }

    private fun loadRecords() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recordList.clear()
                for (recordSnap in snapshot.children) {
                    val record = recordSnap.getValue(MedicalRecord::class.java)
                    record?.let { recordList.add(it) }
                }
                recordsRecycler.adapter = MedicalRecordAdapter(this@MedicalRecordsActivity, recordList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data ?: return
            uploadFile(fileUri)
        }
    }

    private fun uploadFile(uri: Uri) {
        val fileName = UUID.randomUUID().toString()
        val fileRef = storage.child("medicalRecords/$fileName")

        fileRef.putFile(uri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val record = MedicalRecord(fileName, fileName, downloadUrl.toString())
                database.child(fileName).setValue(record)
                Toast.makeText(this, "Uploaded!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload", Toast.LENGTH_SHORT).show()
        }
    }
}
