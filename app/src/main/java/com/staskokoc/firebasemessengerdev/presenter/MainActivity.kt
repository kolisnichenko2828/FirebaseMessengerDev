package com.staskokoc.firebasemessengerdev.presenter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.staskokoc.firebasemessengerdev.databinding.ActivityMainBinding
import com.staskokoc.firebasemessengerdev.domain.models.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessagesAdapter

    private val username: String = "Stas"
    private var tempiterator: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initFirebaseDatabase()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for(s in snapshot.children) {
                    val user = s.getValue(User::class.java)
                    if(user != null) {
                        users.add(user)
                    }
                }
                adapter.submitList(users)
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        binding.textInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty() && s[s.length - 1] == '\n' && s[0] != ' ' && s[0] != '\n') {
                    sendMessage()
                } else if(s.isNotEmpty() && (s[0] == ' ' || s[0] == '\n')) {
                    binding.textInput.text?.clear()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.button.setOnClickListener {
            if(binding.textInput.text.toString().isNotEmpty()) {
                sendMessage()
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        adapter = MessagesAdapter()
        recyclerView.adapter = adapter
    }

    private fun initFirebaseDatabase() {
        database = Firebase.database
        databaseReference = database.getReference("messages")
    }

    private fun sendMessage() {
        val message = binding.textInput.text.toString()
        binding.textInput.text?.clear()
        databaseReference.child("${tempiterator++}").setValue(User(username, message))
    }
}