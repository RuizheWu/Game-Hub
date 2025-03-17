package com.example.myfirstapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.data.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button

    private val FIREBASE_DATABASE_URL = "https://csci4176-group11-project-default-rtdb.firebaseio.com/"
    private lateinit var mAuth:FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signupButton = findViewById(R.id.signupButton)

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Sign in successful", Toast.LENGTH_SHORT).show()
                    val currentUser = mAuth.currentUser
                    currentUser?.uid?.let { userId ->
                        Log.d(TAG, "User ID: $userId")
                        database.child("Registered_Users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userData = snapshot.getValue(User::class.java)
                                userData?.let { user ->
                                    // Sign in success, navigate to user profile activity
                                    user.uid = userId
                                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                    intent.putExtra("userData", user)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(TAG, error.message)
                                // Handle error
                                Toast.makeText(baseContext, "Database error.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.currentUser?.reload()
    }
}