package com.example.myfirstapp

import FeedController
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapters.FeedAdapter
import com.example.myfirstapp.data.User
import com.example.myfirstapp.events.EventHostActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var profileImage: ImageView
    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var feedAdapter: FeedAdapter
    private val controller = FeedController()
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        currentUser = intent.getParcelableExtra("userData") ?: User()
        val profileNameView : TextView = findViewById(R.id.profileName)
        profileNameView.text = currentUser.username
        setupFeed()
        setupBottomNavigation()

        val goToEventButton : Button = findViewById(R.id.go_to_eventFragment)
        goToEventButton.setOnClickListener {
            val intent = Intent(this, EventHostActivity::class.java).apply {
                putExtra("currentUser", currentUser)
            }
            startActivity(intent)
        }




    }

    private fun setupFeed() {
        profileImage = findViewById(R.id.profileImage)
        feedRecyclerView = findViewById(R.id.feedRecyclerView)
        feedRecyclerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedAdapter(listOf()) // Initialize the adapter with an empty list
        feedRecyclerView.adapter = feedAdapter

        // Use FeedController to fetch posts and update the RecyclerView's adapter
        controller.getPosts { posts ->
            feedAdapter.updatePosts(posts)
        }

        if(currentUser.avatarId != 0){
            profileImage.setImageResource(currentUser.avatarId)
        }
        else{
            val defaultImage = ContextCompat.getDrawable(this, R.drawable.amongus)
            profileImage.setImageDrawable(defaultImage)
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_home
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true // No action, user is already in the HomeActivity
                R.id.navigation_group -> {
                    startActivity(Intent(this, GroupsActivity::class.java).apply {
                        putExtra("userData", currentUser)
                    })
                    true
                }
                R.id.navigation_new_post -> {
                    val intent = Intent(this, NewPostActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                R.id.navigation_news_and_update -> {
                    startActivity(Intent(this, NewsAndUpdatesActivity::class.java).apply {
                        putExtra("userData", currentUser)
                    })
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java).apply {
                        putExtra("userData", currentUser)
                    })
                    true
                }
                else -> false
            }
        }
    }
}
