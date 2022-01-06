package com.example.a005_obecnosc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.a005_obecnosc.databinding.ActivityRegisterBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var binding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initData()
        confViews()


    }

    fun initData() {
        mAuth = FirebaseAuth.getInstance()
        database =
            Firebase.database("https://obecnosc-abdfb-default-rtdb.europe-west1.firebasedatabase.app")
    }

    fun confViews() {
        binding.register.setOnClickListener {
            register()
        }
    }

    fun register()  = with(binding){
        val actionCodeSettings = ActionCodeSettings.newBuilder().setHandleCodeInApp(true)
        if (!mail.text.isNullOrEmpty()
            && !password.text.isNullOrEmpty()
            && !passwordConfirm.text.isNullOrEmpty()
            && !name.text.isNullOrEmpty()){
                if(password.text!!.length >= 6) {
                    if (password.text.toString() == passwordConfirm.text.toString())
                    {
                        var actionCodeSettings = ActionCodeSettings.newBuilder()
                        actionCodeSettings.setHandleCodeInApp(true)
                        actionCodeSettings.setUrl("https://obecnosc-abdfb.firebaseapp.com")
                        mAuth.createUserWithEmailAndPassword(
                            binding.mail.text.toString().trim(),
                            binding.password.text.toString().trim()
                        ).addOnSuccessListener {
                            val mRef = database.reference.child("users").child(mAuth.uid.toString())
                            val user = User(binding.name.text.toString(), Status.OFFLINE.name)
                            mRef.setValue(user)
                            mAuth.currentUser?.sendEmailVerification(actionCodeSettings.build())?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showToast(this@RegisterActivity,"Wysłano mail weryfikacyjny")
                                } else {
                                    showToast(this@RegisterActivity,task.exception?.message.toString())
                                }
                            }
                            showToast(this@RegisterActivity, "Wysłano mail weryfikacyjny")
                        }.addOnFailureListener {
                            showToast(this@RegisterActivity, it.message.toString())
                        }
                    } else {
                        showToast(this@RegisterActivity, "Hasła nie są identyczne")
                    }
                } else {
                    showToast(this@RegisterActivity, "Hasło musi być dłuższe niż 5 znaków")
                }
        } else {
            showToast(this@RegisterActivity, "Żadne z pól nie może być puste")
        }

    }
}