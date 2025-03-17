package com.example.myfirstapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.data.User
import com.example.myfirstapp.adapters.LeaderAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class LeaderboardActivity : AppCompatActivity() {
    private val FIREBASE_DATABASE_URL = "https://csci4176-group11-project-default-rtdb.firebaseio.com/"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var dataSet: MutableList<LeaderAdapter.LeaderItem>
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)
        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        dataSet = mutableListOf()
        currentUser = intent.getParcelableExtra("userData") ?: User()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val databaseRef = FirebaseDatabase.getInstance().getReference("Registered_Users")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterate through each user in the database
                for (userSnapshot in dataSnapshot.children) {
                    // Get the username, achievements, and avatar ID for each user
                    val username = userSnapshot.child("username").getValue(String::class.java)
                    val profileId = userSnapshot.child("avatarId").getValue(Int::class.java)

                    // Get achievements snapshot
                    val achievementsSnapshot = userSnapshot.child("achievements")
                    var unlockedAchievementsCount = 0

                    // Iterate through each achievement
                    for (achievementSnapshot in achievementsSnapshot.children) {
                        // Check if the achievement is unlocked
                        val unlocked = achievementSnapshot.child("unlocked").getValue(Boolean::class.java)

                        if (unlocked == true) {
                            unlockedAchievementsCount++
                        }
                    }

                    if (username != null) {
                        // val profileDrawable = getAvatarDrawable(profileId)
                        var profileDrawable = 0
                        if (profileId == 0){
                            profileDrawable = R.drawable.amongus
                        } else {
                            if (profileId != null) {
                                profileDrawable = profileId
                            }
                        }
                        // Create a LeaderItem object with user data
                        val leaderItem = profileDrawable?.let {
                            LeaderAdapter.LeaderItem(
                                username, unlockedAchievementsCount.toString(),
                                it
                            )
                        }
                        if (leaderItem != null) {
                            dataSet.add(leaderItem)
                        }
                    }
                }

                // Sort the dataset by achievements count in descending order
                val sortedDataSet = dataSet.sortedByDescending { it.score.toInt() }
                val indexedDataSet = sortedDataSet.mapIndexed { index, leaderItem ->
                    leaderItem.copy(rank = (index + 1).toString())
                }
                val dataSetWithIndex = indexedDataSet.map {
                    LeaderAdapter.LeaderItem(it.profileName, it.score, it.profilePic, it.rank)
                }


                val adapter = LeaderAdapter(dataSetWithIndex)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException())
            }
        })

    }

    // Function to get drawable resource ID for avatar based on avatar ID
    private fun getAvatarDrawable(profileId: Int?): Int? {
        return when (profileId) {
            0 -> R.drawable.avatar_one
            1 -> R.drawable.avatar_two
            2 -> R.drawable.avatar_three
            3 -> R.drawable.avatar_four
            4 -> R.drawable.avatar_five
            else -> null
        }
    }
}