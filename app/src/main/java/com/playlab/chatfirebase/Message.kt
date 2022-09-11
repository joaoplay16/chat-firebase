package com.playlab.chatfirebase

open class Message(
    val text: String,
    val timestamp: Long,
    val fromId: String,
    val toId: String
){
    constructor(): this("",0,"","")
}