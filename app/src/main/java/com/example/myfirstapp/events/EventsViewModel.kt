package com.example.myfirstapp.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventsViewModel : ViewModel() {

    private val repository = EventsRepository()
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _error = MutableLiveData<String>()

    fun getEvents(category: String) {
        repository.getEventsByCategory(category, { eventsList ->
            _events.value = eventsList
        }, { exception ->
            _error.value = exception.message //
        })
    }
}