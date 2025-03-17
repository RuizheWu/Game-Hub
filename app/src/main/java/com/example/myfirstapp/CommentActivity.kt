package com.example.myfirstapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapters.CommentAdapter
import com.example.myfirstapp.data.Post.Comment
import com.google.firebase.database.*

class CommentActivity : AppCompatActivity() {
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentEditText: EditText
    private lateinit var submitCommentButton: Button

    private lateinit var commentAdapter: CommentAdapter
    private var comments = mutableListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView)
        commentEditText = findViewById(R.id.commentEditText)
        submitCommentButton = findViewById(R.id.submitCommentButton)

        val postId = intent.getStringExtra("postId") ?: return

        // Setup RecyclerView
        commentAdapter = CommentAdapter(comments)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.adapter = commentAdapter

        // Get Comments
        val databaseReference = FirebaseDatabase.getInstance().reference.child("posts").child(postId).child("comments")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comments.clear()
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(Comment::class.java)
                    if (comment != null) {
                        comments.add(comment)
                    }
                }
                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to load comments: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Add new comment
        submitCommentButton.setOnClickListener {
            val commentContent = commentEditText.text.toString().trim()
            if (commentContent.isNotEmpty()) {
                val newComment = Comment(id = databaseReference.push().key ?: "", userId = "user_id", content = commentContent, timestamp = System.currentTimeMillis())
                databaseReference.push().setValue(newComment).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Comment added successfully", Toast.LENGTH_SHORT).show()
                        commentEditText.text.clear()
                    } else {
                        Toast.makeText(applicationContext, "Failed to add comment: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
