package com.example.a005_obecnosc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mailEt : EditText
    private lateinit var passwordEt : EditText
    private lateinit var registerTX : TextView
    private lateinit var login : Button
    private lateinit var mAuth : FirebaseAuth

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mailEt = findViewById(R.id.mail)
        passwordEt = findViewById(R.id.haslo)
        registerTX = findViewById(R.id.rejestracja)
        login = findViewById(R.id.login)
        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null){
            startActivity(Intent(this,button_activity::class.java))
        }

        registerTX.setOnClickListener{
            startActivity(Intent(this,register_activity::class.java))
        }
        login.setOnClickListener {
            mAuth.signInWithEmailAndPassword(mailEt.text.toString(),passwordEt.text.toString()).addOnSuccessListener {
                startActivity(Intent(this,button_activity::class.java))
            }.addOnFailureListener {
                val timer : Int = 2
                Toast.makeText(this,"Cos poszlo nie tak",timer)
            }
        }
    }
}