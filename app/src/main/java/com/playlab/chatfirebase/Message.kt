package com.playlab.chatfirebase

open class Message(
    open val text: String,
    open val timestamp: Long,
    open val fromId: String,
    open val toId: String
){
    constructor(): this("",0,"","")
}