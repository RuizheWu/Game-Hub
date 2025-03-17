package com.example.myfirstapp.events

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R

class CreateEventActivity : AppCompatActivity() {

    private lateinit var eventName: EditText
    private lateinit var eventDescription: EditText
    private lateinit var eventCategoryDropdown: AutoCompleteTextView
    private lateinit var createEventButton: Button
    private val eventsRepository = EventsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        eventName = findViewById(R.id.eventName)
        eventDescription = findViewById(R.id.eventDescription)
        eventCategoryDropdown = findViewById(R.id.eventCategoryDropdown)
        createEventButton = findViewById(R.id.createEventButton)

        setupCategoryDropdown()

        createEventButton.setOnClickListener {
            val event = Event(
                id = "",
                name = eventName.text.toString(),
                description = eventDescription.text.toString(),
                category = eventCategoryDropdown.text.toString(),
                participants = listOf()
            )

            eventsRepository.addEvent(event,
                onSuccess = {
                    Toast.makeText(this, "Event created successfully", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Error creating event: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setupCategoryDropdown() {
        val categories = listOf("Casual", "Competitive")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        eventCategoryDropdown.setAdapter(adapter)
    }
}
