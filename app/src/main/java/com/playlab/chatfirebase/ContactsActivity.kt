package com.playlab.chatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ContactsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)


        fetchUsers()
    }

    private fun fetchUsers() {
        FirebaseFirestore.getInstance().collection("/users")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null){
                    Log.e("LOGGER", error.message.toString(), error)
                    return@addSnapshotListener
                }

                val docs: List<DocumentSnapshot>? = querySnapshot?.documents
                if (docs != null) {
                    for (doc in docs){
                        val user = doc.toObject(User::class.java)
                        Log.d("LOGGER", user?.userName.toString())
                    }
                }
            }
    }
}