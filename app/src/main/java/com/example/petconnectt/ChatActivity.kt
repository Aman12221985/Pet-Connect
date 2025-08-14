package com.example.petconnectt

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var replyPreviewLayout: LinearLayout
    private lateinit var replySenderTextView: TextView
    private lateinit var replyMessageTextView: TextView
    private lateinit var closeReplyButton: ImageButton

    private val messageList = mutableListOf<ChatMessage>()

    private lateinit var databaseRef: DatabaseReference
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val currentUserId = currentUser?.uid ?: ""
    private val currentUserName = currentUser?.displayName ?: "You"

    private var replyingToMessage: ChatMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Initialize views
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        replyPreviewLayout = findViewById(R.id.replyPreviewLayout)
        replySenderTextView = findViewById(R.id.replySenderTextView)
        replyMessageTextView = findViewById(R.id.replyMessageTextView)
        closeReplyButton = findViewById(R.id.closeReplyButton)

        // Setup RecyclerView
        messageAdapter = MessageAdapter(
            messageList,
            currentUserId,
            onEditClicked = { showEditDialog(it) },
            onDeleteClicked = { deleteMessage(it) },
            onReplyClicked = { onReplyMessage(it) }
        )
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        // Firebase reference (replace "chatRoomId" with your actual room logic if needed)
        databaseRef = FirebaseDatabase.getInstance().getReference("chats")

        // Send button
        sendButton.setOnClickListener {
            sendMessage()
        }

        // Close reply preview
        closeReplyButton.setOnClickListener {
            clearReply()
        }

        // Load messages
        listenForMessages()
    }

    private fun sendMessage() {
        val messageText = messageEditText.text.toString().trim()
        if (TextUtils.isEmpty(messageText)) return

        val messageId = databaseRef.push().key ?: return
        val timestamp = System.currentTimeMillis()

        val chatMessage = ChatMessage(
            messageId = messageId,
            senderId = currentUserId,
            senderName = currentUserName,
            message = messageText,
            timestamp = timestamp,
            repliedMessage = replyingToMessage?.message,
            repliedSenderName = replyingToMessage?.senderName
        )

        databaseRef.child(messageId).setValue(chatMessage)
        messageEditText.text.clear()
        clearReply()
    }

    private fun listenForMessages() {
        databaseRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (child in snapshot.children) {
                    val message = child.getValue(ChatMessage::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }
                messageAdapter.notifyDataSetChanged()
                chatRecyclerView.scrollToPosition(messageList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEditDialog(message: ChatMessage) {
        val editText = EditText(this)
        editText.setText(message.message)

        AlertDialog.Builder(this)
            .setTitle("Edit Message")
            .setView(editText)
            .setPositiveButton("Update") { _, _ ->
                val newText = editText.text.toString().trim()
                if (newText.isNotEmpty()) {
                    val updatedMessage = message.copy(message = newText)
                    databaseRef.child(message.messageId).setValue(updatedMessage)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteMessage(message: ChatMessage) {
        AlertDialog.Builder(this)
            .setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { _, _ ->
                databaseRef.child(message.messageId).removeValue()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onReplyMessage(message: ChatMessage) {
        replyingToMessage = message
        replySenderTextView.text = message.senderName
        replyMessageTextView.text = message.message
        replyPreviewLayout.visibility = View.VISIBLE
    }

    private fun clearReply() {
        replyingToMessage = null
        replyPreviewLayout.visibility = View.GONE
    }
}
