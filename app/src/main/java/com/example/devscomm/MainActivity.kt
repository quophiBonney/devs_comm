package com.example.devscomm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.example.devscomm.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding
            .inflate(layoutInflater)
        setContentView(binding.root)

        val txtLogin = findViewById<TextView>(R.id.txtLogin)
        txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //Clicking the signup button to register
        binding.btnSignup.setOnClickListener {
            createAccount()
        }

    }

    //Function of the createAccount
    private fun createAccount() {
        val username = binding.editTextPersonName.text.toString()
        val email = binding.editTextTextEmailAddress2.text.toString()
        val password = binding.editTextTextPassword.text.toString()
        when {
            TextUtils.isEmpty(username) -> Toast.makeText(
                this,
                "Username is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(email) -> Toast.makeText(
                this, "Email is required",
                Toast.LENGTH_LONG)
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
                            saveUserInfo(username, email, password)
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

    private fun saveUserInfo(username: String, email: String, password: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

        val userMap = HashMap<String, Any>()
        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account has been created successfully", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(this, MainPage::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                }
            }
    }
}