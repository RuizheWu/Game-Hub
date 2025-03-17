package com.example.myfirstapp

import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewsUpdatesDB(private val database: FirebaseDatabase) {
    data class NewsItem(
        val title: String? = null,
        val description: String? = null,
        val section: String? = null
    )

    fun retrieveNewsFromDatabase(referencePath: String, section: String, callback: (List<NewsItem>) -> Unit) {
        val databaseRef = database.reference.child(referencePath)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newsItemList = mutableListOf<NewsItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val newsItem = childSnapshot.getValue(NewsItem::class.java)
                    if (newsItem != null && newsItem.section == section) {
                        newsItemList.add(newsItem)
                    }
                }
                callback(newsItemList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
