package com.example.myfirstapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var auth: FirebaseAuth
    private val FIREBASE_DATABASE_URL = "https://csci4176-group11-project-default-rtdb.firebaseio.com/"
    private lateinit var database: DatabaseReference
    private lateinit var textView :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.readTest)
        database = Firebase.database.reference
        connectFirebase()
        writeToFirebaseRealTimeDB()
        readFromFirebaseRealTimeDB()
    }

    private fun connectFirebase() {
        val firebaseDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
        database = firebaseDB.getReference("message")
        Toast.makeText(this, "Firebase connection success", Toast.LENGTH_LONG).show()
    }
//    Test to write to the Firebase DB
    private fun writeToFirebaseRealTimeDB() {
        database.child("Group 11").setValue("GameHub: An Entertainment Social Media")
    }
//    Test to read from Firebase DB
    private fun readFromFirebaseRealTimeDB(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupProject = snapshot.value
                textView.text = groupProject.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        database.child("Group 11").addValueEventListener(postListener)
    }

}