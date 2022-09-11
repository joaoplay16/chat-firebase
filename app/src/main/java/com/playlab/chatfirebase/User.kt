package com.playlab.chatfirebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uuid: String,
    val userName: String,
    val profileUrl: String,
    val token: String,
    val online: Boolean

) : Parcelable{
    constructor(): this("", "","", "", false)
}