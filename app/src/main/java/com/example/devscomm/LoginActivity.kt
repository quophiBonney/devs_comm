package com.example.devscomm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.example.devscomm.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding
            .inflate(layoutInflater)
        setContentView(binding.root)

        val txtSignup = findViewById<TextView>(R.id.txtSignup)
        txtSignup.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            loginAccount()
        }

    }

    private fun loginAccount() {
        val email = binding.editTextTextEmail.text.toString()
        val password = binding.editTextTextPassword.text.toString()

        when {
            TextUtils.isEmpty(email) -> Toast.makeText(
                this,
                "email is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this, "Email is required", Toast.LENGTH_LONG)
                .show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                this,
                "Password is required",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainPage::class.java)
                            startActivity(intent)
                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(
                                this,
                                "Error : $message",
                                Toast.LENGTH_LONG
                            ).show()
                            mAuth.signOut()
                        }
                    }

            }
        }
    }
}