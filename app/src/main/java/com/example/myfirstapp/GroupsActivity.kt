package com.example.myfirstapp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView


class GroupsActivity : AppCompatActivity(), GroupClickListener {

    private lateinit var currentUser: User

    private lateinit var groupsRecyclerView: RecyclerView
    private lateinit var groupsAdapter: GroupsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        currentUser = intent.getParcelableExtra("userData") ?: User()
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView)
        groupsRecyclerView.layoutManager = LinearLayoutManager(this)

        val groups = listOf("WZ", "FC24", "BF", "Forza")

        groupsAdapter = GroupsAdapter(groups, this)
        groupsRecyclerView.adapter = groupsAdapter

        //Monitor Navigation bar

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_group
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

    override fun onGroupClicked(groupName: String) {
        // Handle group item click
        // Redirect to the group page or perform any other action
        // In this example, we'll just log the clicked group name
        Log.d("GroupsActivity", "Group Clicked: $groupName")
        val intent = Intent(this,HomeActivity::class.java)
        intent.putExtra("userData", currentUser)
        startActivity(intent)
    }
}
