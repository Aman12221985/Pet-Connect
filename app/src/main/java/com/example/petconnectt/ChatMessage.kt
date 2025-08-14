package com.example.petconnectt

data class ChatMessage(
    val messageId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val repliedMessage: String? = null,
    val repliedSenderName: String? = null
)
