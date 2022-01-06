package com.example.a005_obecnosc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a005_obecnosc.databinding.ActivityButton2Binding
import com.example.a005_obecnosc.databinding.UserRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.core.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ButtonActivity : AppCompatActivity() {


    private lateinit var status: Status
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var usersList: MutableList<User>

    private lateinit var binding: ActivityButton2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityButton2Binding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        usersList = mutableListOf()
        database =
            Firebase.database("https://obecnosc-abdfb-default-rtdb.europe-west1.firebasedatabase.app")
        mAuth = FirebaseAuth.getInstance()
        var mRef = database.reference.child("users").child(mAuth.uid.toString());
        mRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                val e = snapshot.getValue(User::class.java)
                binding.name.text = e?.name
                if (e?.status == Status.OFFLINE.name) {
                    binding.yourStatus.setBackgroundResource(android.R.drawable.presence_busy)
                    status = Status.OFFLINE
                } else {
                    binding.yourStatus.setBackgroundResource(android.R.drawable.presence_online)
                    status = Status.ONLINE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        mRef = database.reference.child("users")
        mRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for (child in snapshot.children) {
                    if (child.key != mAuth.uid.toString())
                        child.getValue(User::class.java)?.let {
                            usersList.add(it)
                        }
                }
                usersAdapter = UsersAdapter(usersList, applicationContext)
                binding.recyclerView.adapter = usersAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.changeStatusButton.setOnClickListener {
            if (status == Status.ONLINE) status = Status.OFFLINE
            else status = Status.ONLINE
            mAuth.currentUser?.let {
                mRef.child(it.uid).setValue(User(binding.name.text.toString().trim(), status))
            }
        }


    }

    class UsersAdapter(
        private val dataSet: MutableList<User>,
        val context: android.content.Context
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class UserHolder(v: View) : RecyclerView.ViewHolder(v)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            UserHolder(UserItemView(context))

        override fun getItemCount() = dataSet.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder.itemView as UserItemView).assign(
                dataSet[position].name,
                dataSet[position].status
            )
        }

    }

}