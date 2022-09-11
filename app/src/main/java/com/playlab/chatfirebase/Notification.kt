package com.playlab.chatfirebase

class Notification (
    val fromName: String,
    override val text: String,
    override val timestamp: Long,
    override val fromId: String,
    override val toId: String
): Message(
    text = text,
    timestamp = timestamp,
    fromId = fromId,
    toId = toId
)