package com.example.petconnectt.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petconnectt.R
import com.example.petconnectt.model.MedicalRecord

class MedicalRecordAdapter(
    private val context: Context,
    private val records: List<MedicalRecord>
) : RecyclerView.Adapter<MedicalRecordAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileNameText: TextView = view.findViewById(R.id.tvFileName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_medical_record, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = records.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.fileNameText.text = record.fileName

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(record.fileUrl))
            context.startActivity(intent)
        }
    }
}
