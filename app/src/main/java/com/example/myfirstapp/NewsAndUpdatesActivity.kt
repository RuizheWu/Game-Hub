package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myfirstapp.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsAndUpdatesActivity :  AppCompatActivity() {

    private lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_updates_activity)

        currentUser = intent.getParcelableExtra("userData") ?: User()
        val gameButton: ImageButton = findViewById(R.id.game)
        val insightsButton: ImageButton = findViewById(R.id.insights)
        val gamingButton: ImageButton = findViewById(R.id.gaming)
        val latestButton: ImageButton = findViewById(R.id.latest)
        val strategiesButton: ImageButton = findViewById(R.id.strategies)
        val gameReviewsButton: ImageButton = findViewById(R.id.game_reviews)
        val addNews: ImageButton = findViewById(R.id.addNews)

        // add news button
        addNews.setOnClickListener {
            val intent = Intent(this, AddNewsAndUpdatesActivity::class.java)
            startActivity(intent)
        }

        // on click navigate to the chosen activity which will display the fragments

        gameButton.setOnClickListener {
            startChosenNewsActivity("Game of The")
        }

        insightsButton.setOnClickListener {
            startChosenNewsActivity("Insights")
        }

        gamingButton.setOnClickListener {
            startChosenNewsActivity("Gaming")
        }

        latestButton.setOnClickListener {
            startChosenNewsActivity("Latest")
        }

        strategiesButton.setOnClickListener {
            startChosenNewsActivity("Strategies")
        }

        gameReviewsButton.setOnClickListener {
            startChosenNewsActivity("Reviews")
        }

        //Monitor Navigation bar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_news_and_update
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {

                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)

                    true
                }
                R.id.navigation_group -> {
                    val intent = Intent(this, GroupsActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                R.id.navigation_new_post ->{
                    val intent = Intent(this, NewPostActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                R.id.navigation_news_and_update -> {
                    val intent = Intent(this, NewsAndUpdatesActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

    }
    private fun startChosenNewsActivity(fragmentName: String) {
        val intent = Intent(this, ChosenNewsActivity::class.java)
        intent.putExtra("fragmentName", fragmentName)
        intent.putExtra("userData", currentUser)
        startActivity(intent)
    }

}