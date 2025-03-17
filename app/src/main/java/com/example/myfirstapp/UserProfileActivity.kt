package com.example.myfirstapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myfirstapp.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapters.AchievementAdapter
import com.example.myfirstapp.adapters.GameArrayAdapter
import com.example.myfirstapp.data.Game

class UserProfileActivity : AppCompatActivity() {
    private lateinit var currentUser: User
    private lateinit var gameListView: ListView
    private val selectedGames = mutableListOf<Game>()
    private lateinit var gamesAdapter: GameArrayAdapter
    companion object {
        private const val GAME_LIBRARY_REQUEST_CODE = 1
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GAME_LIBRARY_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedGame = data?.getParcelableExtra<Game>("selectedGame")
            if (selectedGame != null) {
                addGameToUserList(selectedGame)
            }
        }
    }
    private fun addGameToUserList(game: Game) {
        if (!selectedGames.any { it.name == game.name }) {
            selectedGames.add(game)
            Log.d("DEBUG", selectedGames[selectedGames.size - 1].name)
            gamesAdapter.add(game)
            gamesAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //take the current user's information
        currentUser = intent.getParcelableExtra("userData") ?: User()
        val profileNameView : TextView = findViewById(R.id.user_profile_name)
        val aboutMeView : TextView = findViewById(R.id.user_about_me)
        val preferenceView : TextView = findViewById(R.id.user_gaming_preferences)
        gameListView = findViewById(R.id.displayGame)
        gamesAdapter = GameArrayAdapter(this, R.layout.activity_game_array_adapter, currentUser.games)
        gameListView.adapter = gamesAdapter

        //display information on screen
        profileNameView.text = currentUser.username
        aboutMeView.text = currentUser.description
        preferenceView.text = currentUser.interest

        //display avatar
        val userAvatar : ImageView = findViewById(R.id.user_avatar)
        if(currentUser.avatarId != 0){
            userAvatar.setImageResource(currentUser.avatarId)
        }
        else{
            val defaultImage = ContextCompat.getDrawable(this, R.drawable.amongus)
            userAvatar.setImageDrawable(defaultImage)
        }

        //Monitor edit button
        val editUserProfileButton : Button = findViewById(R.id.edit_profile_button)
        editUserProfileButton.setOnClickListener{
            val intent = Intent(this, EditUserProfileActivity::class.java)
            intent.putExtra("userData", currentUser)
            startActivity(intent)
        }

        //Monitor View all achievement button
        val goToAchievementActivityButton:Button = findViewById(R.id.go_to_achievements)
        goToAchievementActivityButton.setOnClickListener {
            val intent = Intent(this, AchievementActivity::class.java)
            intent.putExtra("userData", currentUser)
            startActivity(intent)
        }

        //display achievements in RecycleView
        val achievementsRecyclerView: RecyclerView = findViewById(R.id.achievementsRecyclerView)
        val unlockedAchievements = currentUser.achievements.filter { it.unlocked }
        val achievementsAdapter = AchievementAdapter(unlockedAchievements)
        achievementsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        achievementsRecyclerView.adapter = achievementsAdapter

        //Monitor add game button
        val addGameButton : Button = findViewById(R.id.addGame)
        addGameButton.setOnClickListener {
            val intent = Intent(this, GameLibraryActivity::class.java)
            intent.putExtra("userData", currentUser)
            startActivityForResult(intent, GAME_LIBRARY_REQUEST_CODE)
        }

        //Monitor Navigation bar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_profile
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
}