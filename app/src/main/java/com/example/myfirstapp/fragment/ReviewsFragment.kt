package com.example.myfirstapp.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.NewsUpdatesDB
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.NewsAdapter
import com.google.firebase.database.FirebaseDatabase

class ReviewsFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseHelper: NewsUpdatesDB
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reviews, container, false)

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance()
        firebaseHelper = NewsUpdatesDB(database)

        // Initialize RecyclerView
        newsRecyclerView = view.findViewById(R.id.recyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Retrieve news from the database
        firebaseHelper.retrieveNewsFromDatabase("news", "Reviews") { newsItemList ->
            newsAdapter = NewsAdapter(newsItemList, R.layout.news_item)
            newsRecyclerView.adapter = newsAdapter
        }

        return view
    }
}
