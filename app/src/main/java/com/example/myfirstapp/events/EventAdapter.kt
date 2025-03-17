package com.example.myfirstapp.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R

class EventsAdapter(
    private val eventsList: List<Event>
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    // Creates view holders for the event items, inflating the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    // Binds data from an event item to the views in the ViewHolder.
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Get current event based on position.
        val currentEvent = eventsList[position]
        // Set text for event name, description, and category.
        holder.eventNameTextView.text = currentEvent.name
        holder.eventDescriptionTextView.text = currentEvent.description
        holder.eventCategoryTextView.text = currentEvent.category
    }

    // Returns the total number of items in the list.
    override fun getItemCount() = eventsList.size

    // ViewHolder class for event items.
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameTextView: TextView = itemView.findViewById(R.id.eventName)
        val eventDescriptionTextView: TextView = itemView.findViewById(R.id.eventDescription)
        val eventCategoryTextView: TextView = itemView.findViewById(R.id.eventCategory)
        val joinEventButton: Button = itemView.findViewById(R.id.joinButton)
    }
}