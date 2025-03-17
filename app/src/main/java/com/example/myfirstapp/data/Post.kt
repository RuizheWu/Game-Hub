package com.example.myfirstapp.data

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Comment

data class Post(
    var id: String = "",
    var userId: String = "",
    var content: String = "",
    var timestamp: Long = 0L,
    var imageUrl: String? = null, // Optional property for the URL of the uploaded image
    var likes: Int = 0,
    var comments: MutableMap<String, Comment>  = mutableMapOf()
) {
    data class Comment(
        var id: String = "",
        var userId: String = "",
        var content: String = "",
        var timestamp: Long = 0L
    )

    fun uploadPost(imageUri: Uri?, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Upload the image to Firebase Storage if it exists
        if (imageUri != null) {
            val storageReference = FirebaseStorage.getInstance().reference.child("post_images").child("$id.jpg")
            storageReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        imageUrl = uri.toString() // Set the imageUrl property with the URL of the uploaded image
                        uploadPostData(onSuccess, onFailure) // Upload the rest of the post data
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            uploadPostData(onSuccess, onFailure) // If there is no image, upload the rest of the post data directly
        }
    }

    private fun uploadPostData(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("posts").child(id)
        databaseReference.setValue(this)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
