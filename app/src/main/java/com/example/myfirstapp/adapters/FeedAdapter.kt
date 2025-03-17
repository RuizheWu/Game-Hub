package com.example.myfirstapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstapp.CommentActivity
import com.example.myfirstapp.data.Post
import com.example.myfirstapp.R

class FeedAdapter(private var posts: List<Post>) : RecyclerView.Adapter<FeedAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postContent: TextView = view.findViewById(R.id.postContent)
        val postImage1: ImageView = view.findViewById(R.id.addImage1)
        val postImage2: ImageView = view.findViewById(R.id.addImage2)
        val postImage3: ImageView = view.findViewById(R.id.addImage3)
        val likeButton: ImageView = view.findViewById(R.id.likeButton)
        val likeCount: TextView = view.findViewById(R.id.likeCount)
        val commentButton: ImageView = view.findViewById(R.id.commentButton)
        val commentCount: TextView = view.findViewById(R.id.commentCount)

        fun bind(post: Post, adapter: FeedAdapter) {
            postContent.text = post.content
            Glide.with(itemView.context).load(post.imageUrl).into(postImage1)
            Glide.with(itemView.context).load(post.imageUrl).into(postImage2)
            Glide.with(itemView.context).load(post.imageUrl).into(postImage3)
            likeCount.text = "${post.likes}"

            likeButton.setOnClickListener {
                // Increment like count and update UI
                post.likes++
                likeCount.text = "${post.likes}"
                // Optionally, update the database
            }

            commentCount.text = "${post.comments.size}"

            commentButton.setOnClickListener {
                // Here you can implement the logic to add a comment or open the comment page
                // For example, just incrementing the number of comments for demonstration
                val context = itemView.context
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("postId", post.id)
                context.startActivity(intent)
                val newComment = Post.Comment(
                    "new_comment_id",
                    "user_id",
                    "New comment",
                    System.currentTimeMillis()
                )
                post.comments[newComment.id] = newComment
                commentCount.text = "${post.comments.size}"
                adapter.notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], this)

    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}
