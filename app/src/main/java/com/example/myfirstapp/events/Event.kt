package com.example.myfirstapp.events

data class Event(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var category: String = "", // "casual" or "competitive"
    var participants: List<String> = listOf()
)