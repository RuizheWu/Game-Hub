package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapters.AchievementAdapter
import com.example.myfirstapp.data.User
import com.google.firebase.database.FirebaseDatabase

class AchievementActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AchievementAdapter
    private lateinit var backButton: Button
    private lateinit var leaderboardButton: Button
    private lateinit var unlockAllButton: Button
    private lateinit var lockAllButton: Button

    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        backButton = findViewById(R.id.backBtn)
        leaderboardButton = findViewById(R.id.leaderboardBtn)
        //unlockAllButton = findViewById(R.id.unlockAllButton)
        //lockAllButton = findViewById(R.id.lockAllButton)

        currentUser = intent.getParcelableExtra("userData") ?: User()

        currentUser.let { user ->
            adapter = AchievementAdapter(user.achievements)
            recyclerView.adapter = adapter

        }

        backButton.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("userData", currentUser)
            startActivity(intent)
        }

        leaderboardButton.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            intent.putExtra("userData", currentUser)
            startActivity(intent)
        }
    }

    private fun unlockAllAchievements() {
        // Update achievements in currentUser
        currentUser.achievements.forEach { achievement ->
            achievement.unlocked = true
        }

        // Update achievements in Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
        database.child("achievements").setValue(currentUser.achievements)
            .addOnCompleteListener { databaseTask ->
                if (databaseTask.isSuccessful) {
                    Toast.makeText(this, "All achievements unlocked!", Toast.LENGTH_SHORT).show()
                    // Refresh RecyclerView to reflect updated achievements
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Failed to unlock achievements", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun lockAllAchievements() {
        // Update achievements in currentUser
        currentUser.achievements.forEach { achievement ->
            achievement.unlocked = false
        }

        // Update achievements in Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
        database.child("achievements").setValue(currentUser.achievements)
            .addOnCompleteListener { databaseTask ->
                if (databaseTask.isSuccessful) {
                    Toast.makeText(this, "All achievements locked!", Toast.LENGTH_SHORT).show()
                    // Refresh RecyclerView to reflect updated achievements
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Failed to unlock achievements", Toast.LENGTH_SHORT).show()
                }
            }
    }
}