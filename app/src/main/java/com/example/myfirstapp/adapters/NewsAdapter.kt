package com.example.myfirstapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.example.myfirstapp.NewsUpdatesDB.NewsItem
import com.example.myfirstapp.R


class NewsAdapter(private val newsList: List<NewsItem>, private val layoutResId: Int) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.newsTitle.text = currentItem.title ?: ""
        holder.newsDescription.text = currentItem.description ?: ""
    }
    override fun getItemCount() = newsList.size

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
    }
}
