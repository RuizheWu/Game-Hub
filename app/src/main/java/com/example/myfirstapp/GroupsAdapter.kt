package com.example.myfirstapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface GroupClickListener {
    fun onGroupClicked(groupName: String)
}
class GroupsAdapter(private val groups: List<String>, private val listener: GroupClickListener) :
    RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group)
        holder.itemView.setOnClickListener { listener.onGroupClicked(group) }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(groupName: String) {
            itemView.findViewById<TextView>(R.id.groupNameTextView).text = groupName
        }
    }
}
