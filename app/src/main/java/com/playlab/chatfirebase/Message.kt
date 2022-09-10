package com.playlab.chatfirebase

data class Message(
    val text: String,
    val timestamp: Long,
    val fromId: String,
    val toId: String
)