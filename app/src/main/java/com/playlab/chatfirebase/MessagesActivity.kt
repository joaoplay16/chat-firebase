package com.playlab.chatfirebase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.playlab.chatfirebase.databinding.ActivityMessagesBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessagesBinding
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GroupAdapter()

        with(binding){
            recyclerContact.layoutManager = LinearLayoutManager(this@MessagesActivity)
            recyclerContact.adapter = adapter
        }

        verifyAuthentication()

        updateToken()

        fetchLastMessage()
    }

    private fun updateToken() {

        val taskInstalationResult = FirebaseInstallations.getInstance().getToken(false)

        val uid = FirebaseAuth.getInstance().uid

        taskInstalationResult.addOnSuccessListener {  result ->
            if(uid != null){
                FirebaseFirestore.getInstance().collection("users")
                    .document(uid)
                    .update("token", result.token)
            }
        }
    }

    private fun fetchLastMessage() {
        val uid = FirebaseAuth.getInstance().uid!!

        FirebaseFirestore.getInstance().collection("/last-messages")
            .document(uid)
            .collection("contacts")
            .addSnapshotListener{ snapshots , exception ->
                val docChanges = snapshots?.documentChanges

                docChanges?.let {
                    docChanges.map { doc ->
                        if (doc.type == DocumentChange.Type.ADDED){
                            val contact = doc.document.toObject(Contact::class.java)

                            adapter.add(ContactItem(contact))
                        }
                    }
                }
            }
    }

    private fun verifyAuthentication() {
        if (FirebaseAuth.getInstance().uid == null){
            val intent = Intent(this, LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.contacts -> startActivity(Intent(this, ContactsActivity::class.java))
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                verifyAuthentication()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private class ContactItem(val contact: Contact) : Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            val userName = viewHolder.itemView.findViewById<TextView>(R.id.textView)
            val message = viewHolder.itemView.findViewById<TextView>(R.id.textView2)
            val imgPhoto = viewHolder.itemView.findViewById<ImageView>(R.id.imageView)

            userName.text = contact.username
            message.text = contact.lastMessage

            Picasso.get().load(contact.photoUrl).into(imgPhoto)
        }

        override fun getLayout(): Int {
            return R.layout.item_user_message
        }
    }
}