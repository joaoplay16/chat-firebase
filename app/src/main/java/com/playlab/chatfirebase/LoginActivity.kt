package com.playlab.chatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.playlab.chatfirebase.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        with(binding){
            btnEnter.setOnClickListener {
                val email = edtEmail.text
                val password = edtPassword.text
            }
        }
    }
}