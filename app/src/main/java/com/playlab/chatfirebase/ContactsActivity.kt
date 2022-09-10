package com.playlab.chatfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.playlab.chatfirebase.databinding.ActivityContactsBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GroupAdapter<GroupieViewHolder>()
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatActivity::class.java)

            val userItem = item as UserItem
            intent.putExtra("user", userItem.user)
            startActivity(intent)
        }

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
                        adapter.add(UserItem(user!!))
                    }
                }
            }
    }
    private class UserItem(val user: User): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            val txtUsername = viewHolder.itemView.findViewById<TextView>(R.id.textView)
            val imgPhoto = viewHolder.itemView.findViewById<ImageView>(R.id.imageView)

            txtUsername.text = user.userName

            Picasso.get()
                .load(user.profileUrl)
                .into(imgPhoto)
        }

        override fun getLayout() = R.layout.item_user
    }

}