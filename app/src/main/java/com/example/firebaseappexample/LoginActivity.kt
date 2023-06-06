package com.example.firebaseappexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseappexample.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        createAccount()
        login()

        val currentUser=auth.currentUser
        if (currentUser!=null){
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun createAccount(){
        binding.createAccountButton.setOnClickListener {
            val email=binding.emailEditTextLoginActivity.text.toString()
            val password=binding.passwordEditTextLoginActivity.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    val intent=Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun login(){
        binding.loginButton.setOnClickListener {
            val email=binding.emailEditTextLoginActivity.text.toString()
            val password=binding.passwordEditTextLoginActivity.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    val intent=Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}