// EventHostActivity.kt
package com.example.myfirstapp.events

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.data.User

class EventHostActivity : AppCompatActivity() {
    private lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_host)

        currentUser = intent.getParcelableExtra("userData") ?: User()

        // Pass currentUser as fragment argument
        val fragment = EventsListFragment().apply {
            arguments = Bundle().apply {
                putParcelable("currentUser", currentUser)
            }
        }

        // Load fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
