package com.example.petconnectt.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petconnectt.PetDetailsActivity
import com.example.petconnectt.R
import com.example.petconnectt.model.ApplicationData

class ApplicationsAdapter(private val list: List<ApplicationData>) :
    RecyclerView.Adapter<ApplicationsAdapter.ApplicationsViewHolder>() {

    inner class ApplicationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPetName: TextView = itemView.findViewById(R.id.tvPetName)
        val tvApplicationDate: TextView = itemView.findViewById(R.id.tvApplicationDate)
        val tvApplicationStatus: TextView = itemView.findViewById(R.id.tvApplicationStatus)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = itemView.context
                    val application = list[position]

                    val intent = Intent(context, PetDetailsActivity::class.java).apply {
                        putExtra("name", application.petName)
                        // You can put more data if needed (e.g., breed, age, etc.)
                    }

                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application, parent, false)
        return ApplicationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicationsViewHolder, position: Int) {
        val item = list[position]
        holder.tvPetName.text = item.petName
        holder.tvApplicationDate.text = "Date: ${item.date}"
        holder.tvApplicationStatus.text = "Status: ${item.status}"
    }

    override fun getItemCount(): Int = list.size
}
