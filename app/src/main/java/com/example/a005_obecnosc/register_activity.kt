package com.example.a005_obecnosc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class register_activity : AppCompatActivity() {

    private lateinit var imieET : EditText
    private lateinit var nazwiskoET : EditText
    private lateinit var mailET : EditText
    private lateinit var hasloEt : EditText
    private lateinit var potwHasloEt : EditText
    private lateinit var rejestracja : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imieET =  findViewById(R.id.imie)
        nazwiskoET = findViewById(R.id.nazwisko)
        mailET = findViewById(R.id.mail)
        hasloEt = findViewById(R.id.haslo)
        potwHasloEt = findViewById(R.id.potwHaslo)
        rejestracja = findViewById(R.id.rejestracja)
        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database("https://obecnosc-abdfb-default-rtdb.europe-west1.firebasedatabase.app")

        rejestracja.setOnClickListener{
            rejestracja()
        }

    }

    @SuppressLint("WrongConstant")
    fun rejestracja() {
        mAuth.createUserWithEmailAndPassword(mailET.text.toString().trim(),hasloEt.text.toString().trim()).addOnSuccessListener {
            val mRef = database.reference.child("users").child(mAuth.uid.toString())
            val uzytkownik = User(imieET.text.toString() + " " + nazwiskoET.text.toString(),"offline")
            mRef.setValue(uzytkownik)
        }.addOnFailureListener{
            val timer : Int = 3
            Toast.makeText(this, it.message,timer).show()
        }
    }
}