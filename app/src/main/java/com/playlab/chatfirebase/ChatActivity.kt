package com.playlab.chatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.playlab.chatfirebase.databinding.ActivityChatBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private var user: User? = null
    private var me: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.extras?.getParcelable("user")
        supportActionBar?.title = user?.userName

        adapter = GroupAdapter<GroupieViewHolder>()

        val llManager = LinearLayoutManager(this)
        llManager.stackFromEnd = true
        binding.recyclerChat.layoutManager = llManager

        binding.recyclerChat.adapter = adapter

        binding.btnChat.setOnClickListener {
            sendMessage()
        }

        FirebaseFirestore.getInstance().collection("/users")
            .document(FirebaseAuth.getInstance().uid!!)
            .get()
            .addOnSuccessListener {  docSnapshot ->
                me = docSnapshot.toObject(User::class.java)
                fetchMessages()
            }
    }

    private fun fetchMessages() {
        if(me != null){
            val fromId = me!!.uuid
            val toId = user!!.uuid

            FirebaseFirestore.getInstance().collection("/conversations")
                .document(fromId)
                .collection(toId)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot, exception ->
                    val documentChanges = querySnapshot?.documentChanges

                    if(documentChanges != null){
                        for (doc in documentChanges){
                            if(doc.type == DocumentChange.Type.ADDED){
                                val message =  doc.document.toObject(Message::class.java)
                                adapter.add(MessageItem(message))
                            }
                        }
                    }
                }
        }
    }

    private fun sendMessage() {
        val text = binding.edtChat.text.toString()

        val fromId = FirebaseAuth.getInstance().uid!!
        val toId = user?.uuid!!
        val timestamp = System.currentTimeMillis()

        binding.edtChat.text = null

        val message = Message(
            fromId = fromId,
            toId = toId,
            timestamp = timestamp,
            text = text
        )

        if(message.text.isNotEmpty()){
            FirebaseFirestore.getInstance().collection("/conversations")
                .document(fromId)
                .collection(toId)
                .add(message)
                .addOnSuccessListener { docRef ->
                    Log.d("LOGGER", "sender ${docRef.id}")
                    val contact = Contact(
                        uuid = toId,
                        username = user!!.userName,
                        photoUrl = user!!.profileUrl,
                        timestamp = message.timestamp,
                        lastMessage = message.text
                    )

                    FirebaseFirestore.getInstance().collection("/last-messages")
                        .document(fromId)
                        .collection("contacts")
                        .document(toId)
                        .set(contact)

                }
                .addOnFailureListener { e ->
                    Log.d("LOGGER", e.message.toString())
                }

            FirebaseFirestore.getInstance().collection("/conversations")
                .document(toId)
                .collection(fromId)
                .add(message)
                .addOnSuccessListener { docRef ->
                    Log.d("LOGGER", "receiver ${docRef.id}")

                    val contact = Contact(
                        uuid = toId,
                        username = user!!.userName,
                        photoUrl = user!!.profileUrl,
                        timestamp = message.timestamp,
                        lastMessage = message.text
                    )

                    FirebaseFirestore.getInstance().collection("/last-messages")
                        .document(toId)
                        .collection("contacts")
                        .document(fromId)
                        .set(contact)
                }
                .addOnFailureListener { e ->
                    Log.d("LOGGER", e.message.toString())
                }
        }

    }

    private inner class MessageItem(private var message: Message): Item<GroupieViewHolder>(){

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            val txtMessage =  viewHolder.itemView.findViewById<TextView>(R.id.txt_message)
            val imgMessage =  viewHolder.itemView.findViewById<ImageView>(R.id.img_message_user)

            txtMessage.text = message.text
            Picasso.get().load(user?.profileUrl).into(imgMessage)

        }

        override fun getLayout(): Int {
            return if (message.fromId == FirebaseAuth.getInstance().uid)
                R.layout.item_from_message
            else R.layout.item_to_message
        }
    }
}