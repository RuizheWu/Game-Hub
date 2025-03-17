package com.example.myfirstapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R

class LeaderAdapter(private val dataSet: List<LeaderItem>) :
    RecyclerView.Adapter<LeaderAdapter.ViewHolder>() {

    data class LeaderItem(val profileName: String, val score: String, val profilePic: Int,val rank: String = "")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileName: TextView = view.findViewById(R.id.profileName)
        val score: TextView = view.findViewById(R.id.score)
        val profilePic: ImageView = view.findViewById(R.id.profilePicture)
        val rank: TextView = view.findViewById(R.id.rank)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.leader_recyclerview_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.profileName.text = item.profileName
        viewHolder.score.text = "Score: ${item.score}"
        viewHolder.profilePic.setImageResource(item.profilePic)
        viewHolder.rank.text = "Rank ${item.rank}"
    }

    override fun getItemCount() = dataSet.size
}