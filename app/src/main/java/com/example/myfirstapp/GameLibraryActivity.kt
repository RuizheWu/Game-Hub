package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirstapp.adapters.GameArrayAdapter
import com.example.myfirstapp.data.Game
import com.example.myfirstapp.data.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// Predefined Game list
val gameList = listOf(
        Game("Counter-Strike 2", R.drawable.game_cs2),
        Game("PUBG", R.drawable.game_pubg),
        Game("APEX", R.drawable.game_apex),
        Game("Fortnite", R.drawable.game_fortnite),
        Game("ROBLOX", R.drawable.game_roblox),
        Game("League of Legends", R.drawable.game_lol),
        Game( "Call of Duty",  R.drawable.game_cod),
        Game("Valorant", R.drawable.game_valorant),
        Game( "Grand Theft Auto V", R.drawable.game_gta5)
)
class GameLibraryActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var currentUser: User
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get current user data from intent
        currentUser = intent.getParcelableExtra("userData") ?: User()
        database = FirebaseDatabase.getInstance()
        userRef = database.reference.child("Registered_Users")
        listView = findViewById(R.id.listView)

        val adapter = GameArrayAdapter(
            this,
            R.layout.activity_game_array_adapter,
            gameList
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedGame = gameList[position]
            addGameToUserProfile(selectedGame)
        }
    }

    // Function to add a selected game to the user profile
    private fun addGameToUserProfile(selectedGame: Game) {
        // Check if the game is not already added to the user's profile
        if (!currentUser.games.contains(selectedGame)) {
            currentUser.games += selectedGame

            // Update the user's games list in the Firebase database
            userRef.child(currentUser.uid).child("games").setValue(currentUser.games)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Game added successfully", Toast.LENGTH_SHORT).show()

                        // Prepare intent to return the selected game and updated user data
                        val returnIntent = Intent()
                        returnIntent.putExtra("selectedGame", selectedGame)
                        returnIntent.putExtra("userData", currentUser)
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add game", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "You have already added this game", Toast.LENGTH_SHORT).show()
        }
    }
}