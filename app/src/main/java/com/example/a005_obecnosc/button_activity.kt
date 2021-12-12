package com.example.a005_obecnosc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a005_obecnosc.databinding.UserRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.core.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class button_activity : AppCompatActivity() {

    private lateinit var status : Button
    private lateinit var lista : Button
    private lateinit var statusVar : String
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var yourName: TextView
    private lateinit var yourStatus: ImageView
    private lateinit var usersList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button2)

        yourName = findViewById(R.id.nameTV)
        yourStatus = findViewById(R.id.statusImage)
        usersRecyclerView = findViewById(R.id.recyclerView)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersList = mutableListOf()
        database =
            Firebase.database("https://obecnosc-abdfb-default-rtdb.europe-west1.firebasedatabase.app")
        mAuth = FirebaseAuth.getInstance()
        var mRef = database.reference.child("users").child(mAuth.uid.toString());
        mRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                val e = snapshot.getValue(User::class.java)
                yourName.text = e?.name
                if ( e?.status == "offline")
                    yourStatus.setBackgroundResource(android.R.drawable.presence_busy)
                else
                    yourStatus.setBackgroundResource(android.R.drawable.presence_online)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        mRef = database.reference.child("users")
        mRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children){
                    if(child.key != mAuth.uid.toString())
                    child.getValue(User::class.java)?.let {
                         usersList.add(it)
                    }
                }
                usersAdapter = UsersAdapter(usersList,applicationContext)
                usersRecyclerView.adapter = usersAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    class UsersAdapter(private val dataSet: MutableList<User>,
        val context: android.content.Context
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        inner class UserHolder(v: View) : RecyclerView.ViewHolder(v)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = UserHolder(UserItemView(context))

        override fun getItemCount() = dataSet.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder.itemView as UserItemView).assign(
                dataSet[position].name,
                dataSet[position].status
            )
        }

    }

}