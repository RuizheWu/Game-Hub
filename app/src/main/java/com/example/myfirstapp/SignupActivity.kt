package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.data.Achievement
import com.example.myfirstapp.data.Game
import com.example.myfirstapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var returnLogin: TextView

    private lateinit var mAuth:FirebaseAuth
    private lateinit var registeredUserDB:DatabaseReference // <---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signupButton = findViewById(R.id.signupButton)
        signupButton.setOnClickListener(this)
        returnLogin = findViewById<View>(R.id.registerReturn) as TextView
        returnLogin.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        registeredUserDB = FirebaseDatabase.getInstance().reference.child("Registered_Users") //<--

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                register()
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.signupButton -> register()
            R.id.registerReturn -> startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun register() {
        val emailString = emailEditText.text.toString().trim { it <= ' ' }
        val pwdString: String = passwordEditText.text.toString().trim { it <= ' ' }

        if (emailString.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            emailEditText.error = "Invalid Format"
            emailEditText.requestFocus()
            return
        }

        if (pwdString.isEmpty() || pwdString.length < 6) {
            passwordEditText.error = "Invalid Password"
            passwordEditText.requestFocus()
            return
        }

        mAuth.createUserWithEmailAndPassword(emailString, pwdString)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = User( emailString, pwdString)
                    FirebaseDatabase.getInstance().getReference("Registered_Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user).addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                val currentUser = mAuth.currentUser
                                val userId = currentUser?.uid
                                // Default values for description, interests, and achievements
                                val defaultDescription = "None"
                                val defaultInterests = "None"

                                //default avatar
                                val defaultAvatar = 0
                                val defaultGames = listOf<Game>()
                                val defaultAchievements = listOf(
                                    Achievement(
                                        "Welcome Aboard",
                                        "Registered an account",
                                        true,
                                        resources.getIdentifier(
                                            "achievement_welcome_aboard",
                                            "drawable",
                                            packageName
                                        )
                                    ),
                                    Achievement(
                                        "Fancy Name",
                                        "Edit your username",
                                        false,
                                        resources.getIdentifier(
                                            "achievement_fancy_name",
                                            "drawable",
                                            packageName
                                        )
                                    ),
                                    Achievement(
                                        "Now You Know Me",
                                        "Edit your user description",
                                        false,
                                        resources.getIdentifier(
                                            "achievement_now_you_know_me",
                                            "drawable",
                                            packageName
                                        )
                                    ),
                                    Achievement(
                                        "That's What I Like",
                                        "Edit your interest",
                                        false,
                                        resources.getIdentifier(
                                            "achievement_what_i_like",
                                            "drawable",
                                            packageName
                                        )
                                    )
                                )

                                val newUser = User(
                                    userId!!,
                                    emailString,
                                    userId,
                                    pwdString,
                                    defaultDescription,
                                    defaultInterests,
                                    //default avatar
                                    defaultAvatar,
                                    defaultGames,
                                    defaultAchievements
                                )

                                registeredUserDB.child(userId).setValue(newUser)
                                    .addOnCompleteListener { databaseTask ->
                                        if (databaseTask.isSuccessful) {
                                            // User data saved successfully
                                            Toast.makeText(baseContext, "Account created", Toast.LENGTH_SHORT).show()

                                            // Navigate to main activity or do further processing
                                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            // Database write failed
                                            Toast.makeText(baseContext, "Database Error.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(baseContext, "Failed to make account.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }
}