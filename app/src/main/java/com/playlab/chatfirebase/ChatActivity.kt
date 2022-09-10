package com.playlab.chatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.playlab.chatfirebase.databinding.ActivityChatBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.extras?.getParcelable<User>("user")
        supportActionBar?.title = user?.userName

        adapter = GroupAdapter<GroupieViewHolder>()
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerChat.adapter = adapter

        adapter.add(MessageItem(isLeft = true))
        adapter.add(MessageItem(isLeft = false))
        adapter.add(MessageItem(isLeft = true))
        adapter.add(MessageItem(isLeft = false))
        adapter.add(MessageItem(isLeft = false))
        adapter.add(MessageItem(isLeft = true))
    }

    private class MessageItem(private var isLeft: Boolean): Item<GroupieViewHolder>(){

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        }

        override fun getLayout() =
            if (isLeft) R.layout.item_from_message else R.layout.item_to_message
    }
}