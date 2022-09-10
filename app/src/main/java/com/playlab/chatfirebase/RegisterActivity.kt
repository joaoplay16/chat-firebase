package com.playlab.chatfirebase

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.playlab.chatfirebase.databinding.ActivityRegisterBinding
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    val launcherImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        try {

        }catch (e: IOException){
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val bitmapDrawable = BitmapDrawable(resources, bitmap)
            binding.btnSelectPhoto.alpha = 0.0f
            binding.imgPhoto.setImageDrawable(bitmapDrawable)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnEnter.setOnClickListener {
                createUser()
            }

            btnSelectPhoto.setOnClickListener {
                selectPhoto()
            }
        }
    }

    private fun selectPhoto() {
        launcherImage.launch("image/*")
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