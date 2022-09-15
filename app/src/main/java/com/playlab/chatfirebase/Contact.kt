package com.playlab.chatfirebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val uuid: String,
    val username: String,
    val lastMessage: String,
    val timestamp: Long,
    val photoUrl: String
    ): Parcelable {

    constructor(): this("","",
        "",0, "")

}