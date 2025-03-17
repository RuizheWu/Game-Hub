package com.example.myfirstapp.events

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R

class EventsListFragment : Fragment() {

    private lateinit var viewModel: EventsViewModel
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var addEventButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_events_list, container, false)

        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Setup the "Add Event" button
        addEventButton = view.findViewById(R.id.addEventButton)
        addEventButton.setOnClickListener {
            startActivity(Intent(context, CreateEventActivity::class.java))
        }

        viewModel.events.observe(viewLifecycleOwner) { events ->
            eventsRecyclerView.adapter = EventsAdapter(events)
        }

        viewModel.getEvents("casual")

        return view
    }
}