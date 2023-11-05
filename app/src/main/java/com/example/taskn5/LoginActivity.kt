package com.example.taskn5

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.example.taskn5.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: AppCompatEditText
    private lateinit var passwordEditText: AppCompatEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        emailEditText = binding.loginEmailEt
        passwordEditText = binding.loginPasswordEt
        binding.backFromLogin.setOnClickListener {
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email= checkIfEmpty(emailEditText)
            val password = checkIfEmpty(passwordEditText)
            email?.let {
                password?.let {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, ProfileActivity::class.java))
                            } else {
                                showToast(baseContext, "Authentication failed.")
                            }
                        }
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkIfEmpty(editText: AppCompatEditText): String?{
        return editText.text.toString().ifEmpty {
            showToast(this, "Fill all fields.")
            null
        }
    }

    private fun showToast(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}