package com.playlab.chatfirebase

class Notification (val fromName: String): Message(){
    constructor(
        fromId: String,
        toId: String,
        timestamp: Long,
        text: String,
        fromName: String
    ) : this(fromName)
}