package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.myfirstapp.data.User
import com.example.myfirstapp.fragment.GameOfTheFragment
import com.example.myfirstapp.fragment.GamingFragment
import com.example.myfirstapp.fragment.LatestFragment
import com.example.myfirstapp.fragment.PlayerInsightsFragment
import com.example.myfirstapp.fragment.ReviewsFragment
import com.example.myfirstapp.fragment.StrategiesFragment


class ChosenNewsActivity : AppCompatActivity() {
    private lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chosen_news_activity)

        currentUser = intent.getParcelableExtra("userData") ?: User()
        val backBtn = findViewById<Button>(R.id.backBtn)

        // go back button
        backBtn.setOnClickListener {
            val intent = Intent(this, NewsAndUpdatesActivity::class.java)
            intent.putExtra("userData", currentUser)
            startActivity(intent)
        }

        val fragmentName = intent.getStringExtra("fragmentName")
        if (fragmentName != null) {
            // display the fragment based on the chosen item in the last activity
            when (fragmentName) {
                "Game of The" -> navigateToFragment(GameOfTheFragment())
                "Insights" -> navigateToFragment(PlayerInsightsFragment())
                "Gaming" -> navigateToFragment(GamingFragment())
                "Latest" -> navigateToFragment(LatestFragment())
                "Strategies" -> navigateToFragment(StrategiesFragment())
                "Reviews" -> navigateToFragment(ReviewsFragment())
            }

        }
    }

        private fun navigateToFragment(fragment: Fragment) {
            val fragmentManager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
}