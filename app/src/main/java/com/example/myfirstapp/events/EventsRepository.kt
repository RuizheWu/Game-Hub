package com.example.myfirstapp.events

import com.google.firebase.firestore.FirebaseFirestore

class EventsRepository {

    private val db = FirebaseFirestore.getInstance()

    // To fetch events details from db
    fun getEventsByCategory(category: String, onSuccess: (List<Event>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("Events")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                val events = result.toObjects(Event::class.java)
                onSuccess(events)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // To aad an event to db
    fun addEvent(event: Event, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("Events")
            .add(event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}