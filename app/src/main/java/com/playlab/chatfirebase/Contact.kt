package com.playlab.chatfirebase

data class Contact(
    val uuid: String,
    val username: String,
    val lastMessage: String,
    val timestamp: Long,
    val photoUrl: String
    ) {

    constructor(): this("","",
        "",0, "")

}