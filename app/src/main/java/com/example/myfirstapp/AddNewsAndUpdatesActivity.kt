package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import android.widget.EditText
import com.google.firebase.database.ValueEventListener

class AddNewsAndUpdatesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_news_activity)

        val titleText = findViewById<TextView>(R.id.titleText)
        val description = findViewById<TextView>(R.id.Description)
        val dropdown = findViewById<Spinner>(R.id.sectionSpinner)

        val backBtn = findViewById<Button>(R.id.backBtn)
        val addBtn = findViewById<Button>(R.id.addBtn)

        /*
        Spinner reference URL: https://developer.android.com/develop/ui/views/components/spinner
         */

        // array of sections
        val sectionItems = arrayOf("Game of The", "Insights",  "Gaming",  "Latest",  "Strategies",  "Reviews")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sectionItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdown.adapter = adapter

        var selectedSection = ""

        //  spinner changes
        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSection = sectionItems[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Go back button
        backBtn.setOnClickListener {
            val intent = Intent(this, NewsAndUpdatesActivity::class.java)
            startActivity(intent)
        }

        // Firebase setup
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val newsRef: DatabaseReference = database.getReference("news")

        // add the news node if it doesn't exist
        val databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("news").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference.child("news").setValue(true)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        // Add news button onclick
        addBtn.setOnClickListener {
            val newsId = newsRef.push().key

            // Check if newsId is null
            newsId?.let {
                val newsItem = hashMapOf(
                    "title" to titleText.text.toString(),
                    "description" to description.text.toString(),
                    "section" to selectedSection
                )

                newsRef.child(newsId).setValue(newsItem)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added news successfully", Toast.LENGTH_SHORT).show()
                        titleText.text
                        description.text
                        dropdown.setSelection(0)
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to add news", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
