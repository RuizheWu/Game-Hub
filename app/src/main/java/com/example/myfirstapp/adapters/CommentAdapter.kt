package com.example.myfirstapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.Post.Comment

class CommentAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commentContent: TextView = view.findViewById(R.id.commentContent)
        val commentTimestamp: TextView = view.findViewById(R.id.commentTimestamp)

        fun bind(comment: Comment) {
            commentContent.text = comment.content
            // Format the timestamp to a readable format
            commentTimestamp.text = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", comment.timestamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size
}
