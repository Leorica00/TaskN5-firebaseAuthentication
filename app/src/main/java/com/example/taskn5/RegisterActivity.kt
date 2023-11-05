package com.example.taskn5

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.taskn5.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private val emailPattern = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    private val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$"
    private val usernamePattern = "^[A-Za-z]\\w{5,29}$"
    private lateinit var emailEditText: AppCompatEditText
    private lateinit var passwordEditText: AppCompatEditText
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        emailEditText = binding.registerEmailEt
        passwordEditText = binding.registerPasswordEt
        val goBackBtn: AppCompatButton = binding.backFromRegister

        goBackBtn.setOnClickListener {
            if (binding.registerLayoutFirst.visibility == View.VISIBLE)
                finish()
            else{
                binding.registerLayoutFirst.visibility = View.VISIBLE
                binding.registerLayoutSecond.visibility = View.GONE
            }
        }

        binding.registerNextBtn.setOnClickListener {
            val email = emailValidation(emailEditText)
            val password = passwordValidation(passwordEditText)
            email?.let {
                password?.let {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                user = auth.currentUser!!
                                binding.registerLayoutFirst.visibility = View.GONE
                                binding.registerLayoutSecond.visibility = View.VISIBLE
                                binding.backFromRegister.visibility = View.GONE

                            }else {
                                showToast(baseContext, "Authentication failed.")
                            }
                        }
                }
            }
        }

        binding.registerBtn.setOnClickListener {
            val username = getUsername(binding.registerUsernameEt)
            username?.let {
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }
                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast(this, "Congrats")
                            startActivity(Intent(this, ProfileActivity::class.java))
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

    private fun emailValidation(editText: AppCompatEditText): String?{
        val email: String = editText.text.toString()
        return if (email.isEmpty() || !email.matches(emailPattern.toRegex())) {
            showToast(this, "Email is not valid.")
            null
        }else{
            email
        }
    }

    private fun passwordValidation(editText: AppCompatEditText): String?{
        val password: String = editText.text.toString()
        return if (password.isEmpty() || !password.matches(passwordPattern.toRegex())) {
            showToast(this, "Password is not valid")
            null
        }else{
            password
        }
    }

    private fun getUsername(editText: AppCompatEditText): String?{
        val username: String = editText.text.toString()
        return if (username.isEmpty() || !username.matches((usernamePattern.toRegex()))) {
            showToast(this, "Username is not valid")
            null
        }else{
            username
        }
    }

    private fun showToast(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}