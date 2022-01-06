package com.example.a005_obecnosc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mailEt : EditText
    private lateinit var passwordEt : EditText
    private lateinit var registerTX : TextView
    private lateinit var login : Button
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mailEt = findViewById(R.id.mail)
        passwordEt = findViewById(R.id.password)
        registerTX = findViewById(R.id.register)
        login = findViewById(R.id.login)
        mAuth = FirebaseAuth.getInstance()

        mAuth.currentUser?.let{
            if(it.isEmailVerified) startActivity(Intent(this,ButtonActivity::class.java))
        }



        registerTX.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        login.setOnClickListener {
            mAuth.signInWithEmailAndPassword(mailEt.text.toString(),passwordEt.text.toString()).addOnSuccessListener {
                mAuth.currentUser?.let{
                    if(it.isEmailVerified) startActivity(Intent(this,ButtonActivity::class.java))
                    else showToast(this@MainActivity, "Email nie został zweryfikowany" )
                }
                startActivity(Intent(this,ButtonActivity::class.java))
            }.addOnFailureListener {
                showToast(this, "Coś poszło nie tak")
            }
        }
    }
}