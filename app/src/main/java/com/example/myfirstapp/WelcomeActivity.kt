package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcomeActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.welcomeLoginButton)
        val registerButton = findViewById<Button>(R.id.welcomeRegisterButton)

        loginButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            finish()
        })
        registerButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@WelcomeActivity, SignupActivity::class.java))
            finish()
        })
    }
}