package com.example.petconnectt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(
    private val messages: List<ChatMessage>,
    private val currentUserId: String,
    private val onEditClicked: (ChatMessage) -> Unit,
    private val onDeleteClicked: (ChatMessage) -> Unit,
    private val onReplyClicked: (ChatMessage) -> Unit
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val replyLayout: LinearLayout = itemView.findViewById(R.id.replyLayout)
        val replySenderTextView: TextView = itemView.findViewById(R.id.replySenderTextView)
        val replyMessageTextView: TextView = itemView.findViewById(R.id.replyMessageTextView)
        val senderTextView: TextView = itemView.findViewById(R.id.senderTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val actionButtonsLayout: LinearLayout = itemView.findViewById(R.id.actionButtonsLayout)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.senderTextView.text = message.senderName
        holder.messageTextView.text = message.message

        // Format timestamp
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        holder.timeTextView.text = sdf.format(Date(message.timestamp))

        // Show reply preview if available
        if (!message.repliedMessage.isNullOrEmpty()) {
            holder.replyLayout.visibility = View.VISIBLE
            holder.replySenderTextView.text = message.repliedSenderName ?: "Unknown"
            holder.replyMessageTextView.text = message.repliedMessage
        } else {
            holder.replyLayout.visibility = View.GONE
        }

        // Show/hide action buttons on long press for sender only
        if (message.senderId == currentUserId) {
            holder.itemView.setOnLongClickListener {
                holder.actionButtonsLayout.visibility =
                    if (holder.actionButtonsLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                true
            }
        } else {
            holder.itemView.setOnLongClickListener(null)
        }

        // Edit button
        holder.editButton.setOnClickListener {
            holder.actionButtonsLayout.visibility = View.GONE
            onEditClicked(message)
        }

        // Delete button
        holder.deleteButton.setOnClickListener {
            holder.actionButtonsLayout.visibility = View.GONE
            onDeleteClicked(message)
        }

        // Click on message to trigger reply
        holder.itemView.setOnClickListener {
            onReplyClicked(message)
        }

        // Apply background and text color based on sender
        if (message.senderId == currentUserId) {
            holder.messageTextView.setBackgroundResource(R.drawable.bg_message_sent) // Blue bubble
            holder.messageTextView.setTextColor(holder.itemView.context.getColor(android.R.color.white)) // White text
        } else {
            holder.messageTextView.setBackgroundResource(R.drawable.bg_message_received) // White bubble
            holder.messageTextView.setTextColor(holder.itemView.context.getColor(android.R.color.black)) // Black text
        }
    }

    override fun getItemCount(): Int = messages.size
}
