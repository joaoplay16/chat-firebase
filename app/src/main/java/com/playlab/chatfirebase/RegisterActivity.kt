package com.playlab.chatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.playlab.chatfirebase.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnEnter.setOnClickListener {
                createUser()
            }
        }


    }

    private fun createUser(){
        with(binding){
            val email = edtEmail.text.toString()
            val senha = edtPassword.text.toString()

            if(email.isNullOrEmpty() || senha.isNullOrEmpty()){
                Toast.makeText(
                    this@RegisterActivity,
                    "Senha e Email devem ser preenchidos",
                    Toast.LENGTH_SHORT)
                    .show()
                return
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener { task ->
                    Toast.makeText(
                        this@RegisterActivity,
                        task.user?.uid,
                        Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this@RegisterActivity,
                        exception.message,
                        Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
}