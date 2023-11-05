package com.example.taskn5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskn5.databinding.ActivityProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.backFromProfile.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val currentUser = Firebase.auth.currentUser
        currentUser?.let {
            binding.userEmail.text = it.email
            binding.userUsername.text = it.displayName
        }

        binding.signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}