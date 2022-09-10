package com.playlab.chatfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.playlab.chatfirebase.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnEnter.setOnClickListener {
                val email = edtEmail.text.toString()
                val senha = edtPassword.text.toString()

                if(email.isNullOrEmpty() || senha.isNullOrEmpty()){
                    Toast.makeText(
                        this@LoginActivity,
                        "Senha e Email devem ser preenchidos",
                        Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        Log.d("LOGGER", task.result.user?.uid.toString())

                        val intent = Intent(
                            this@LoginActivity,
                            MessagesActivity::class.java
                        )
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Log.d("LOGGER", e.message.toString())
                    }

            }

            txtAccount.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}