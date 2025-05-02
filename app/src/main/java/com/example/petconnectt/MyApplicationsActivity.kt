package com.example.petconnectt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petconnectt.adapter.ApplicationsAdapter
import com.example.petconnectt.model.ApplicationData


class MyApplicationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_applications)

        val recyclerView: RecyclerView = findViewById(R.id.rvApplications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample Data
        val applicationList = listOf(
            ApplicationData("Tommy", "01 Apr 2025", "Pending"),
            ApplicationData("Lucy", "28 Mar 2025", "Approved"),
            ApplicationData("Rocky", "15 Mar 2025", "Rejected")
        )

        val adapter = ApplicationsAdapter(applicationList)
        recyclerView.adapter = adapter
    }
}
