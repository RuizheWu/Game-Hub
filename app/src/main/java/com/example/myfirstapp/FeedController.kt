import com.example.myfirstapp.data.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class FeedController {
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("posts")
    private val posts = mutableListOf<Post>()

    fun getPosts(onPostsReceived: (List<Post>) -> Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts = mutableListOf<Post>()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let { posts.add(it) }
                }
                posts.sortByDescending { it.timestamp }
                onPostsReceived(posts)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    fun addPost(userId: String, content: String, imageUrl: String) {
        val newPost = Post(
            id = UUID.randomUUID().toString(),
            userId = userId,
            content = content,
            imageUrl = imageUrl,
            timestamp = System.currentTimeMillis(),
            likes = 0,
            comments = mutableMapOf()
        )
        databaseReference.push().setValue(newPost) // Add to Firebase database
        posts.add(0, newPost) // Add to the beginning of the local list to simulate a feed
    }

    fun addComment(postId: String, userId: String, content: String) {
        val post = posts.find { it.id == postId } ?: return
        val commentId = UUID.randomUUID().toString()
        val comment = Post.Comment(
            id = commentId,
            userId = userId,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        post.comments[commentId] = comment // Add comment to the local post's comments map
        databaseReference.child(postId).child("comments").child(commentId).setValue(comment) // Update Firebase database
    }

}
