package com.playlab.chatfirebase

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.playlab.chatfirebase.databinding.ActivityRegisterBinding
import java.io.IOException
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var mSelectedUri: Uri? = null

    val launcherImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        mSelectedUri = uri
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val bitmapDrawable = BitmapDrawable(resources, bitmap)
            binding.btnSelectPhoto.alpha = 0.0f
            binding.imgPhoto.setImageDrawable(bitmapDrawable)
        }catch (e: IOException){

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
            val nome = edtNome.text.toString()
            val email = edtEmail.text.toString()
            val senha = edtPassword.text.toString()

            if(nome.isNullOrEmpty()|| email.isNullOrEmpty() || senha.isNullOrEmpty()){
                Toast.makeText(
                    this@RegisterActivity,
                    "Senha e Email devem ser preenchidos",
                    Toast.LENGTH_SHORT)
                    .show()
                return
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener { task ->
                    Log.d("LOGGER", task.user?.uid.toString())

                    saveUserInFirebase()
                }
                .addOnFailureListener { exception ->
                    Log.d("LOGGER", exception.message.toString())
                }
        }
    }

    private fun saveUserInFirebase() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(mSelectedUri!!)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener{ uri ->
                    Log.d("LOGGER", uri.toString())

                    val uuid = FirebaseAuth.getInstance().uid!!
                    val userName = binding.edtNome.text.toString()
                    val profileUrl = uri.toString()

                    val user = User(uuid, userName, profileUrl)

                    FirebaseFirestore.getInstance().collection("users")
                        .add(user)
                        .addOnSuccessListener { docRef ->
                            Log.d("LOGGER", docRef.id)

                        }
                        .addOnFailureListener { exception ->
                            Log.d("LOGGER", exception.message.toString())

                        }
                }
            }
            .addOnFailureListener {
                Log.d("LOGGER", it.toString())

            }
    }
}