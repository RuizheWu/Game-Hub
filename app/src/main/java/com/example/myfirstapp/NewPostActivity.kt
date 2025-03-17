package com.example.myfirstapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import com.example.myfirstapp.data.Post
import com.example.myfirstapp.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class NewPostActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var postEditText: EditText
    private lateinit var addScreenshotLabel: TextView
    private lateinit var screenshotsGrid: GridLayout
    private lateinit var sharePostButton: Button
    private lateinit var addImage1: ImageButton
    private lateinit var addImage2: ImageButton
    private lateinit var addImage3: ImageButton
    private val REQUEST_CODE_PERMISSION = 100
    private val REQUEST_CODE_PICK_IMAGE = 101
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        currentUser = intent.getParcelableExtra("userData") ?: User()

        // Initialize views
        profileImage = findViewById(R.id.profileImage)
        postEditText = findViewById(R.id.postEditText)
        addScreenshotLabel = findViewById(R.id.addScreenshotLabel)
        screenshotsGrid = findViewById(R.id.screenshotsGrid)
        sharePostButton = findViewById(R.id.sharePostButton)

        // Initialize image buttons
        addImage1 = findViewById(R.id.addImage1)
        addImage2 = findViewById(R.id.addImage2)
        addImage3 = findViewById(R.id.addImage3)

        // Set click listeners for image buttons
        addImage1.setOnClickListener { checkAndRequestPermissions() }
        addImage2.setOnClickListener { checkAndRequestPermissions() }
        addImage3.setOnClickListener { checkAndRequestPermissions() }

        // Set click listener for the share post button
        sharePostButton.setOnClickListener {
            val content = postEditText.text.toString().trim()
            if (content.isNotEmpty()) {
                uploadPost(content)
            } else {
                Toast.makeText(this, "Post content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        setupBottomNavigation()

        if(currentUser.avatarId != 0){
            profileImage.setImageResource(currentUser.avatarId)
        }
        else{
            val defaultImage = ContextCompat.getDrawable(this, R.drawable.amongus)
            profileImage.setImageDrawable(defaultImage)
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE) {
            data?.data?.let { uri ->
                val storageReference = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}")
                storageReference.putFile(uri).addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Handle the image URL after upload
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to upload image: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadPost(content: String) {
        // You can modify this method to upload the post with the image URL
        val post = Post(
            id = UUID.randomUUID().toString(),
            userId = "user_id",
            content = content,
            imageUrl = "", // Set the image URL after uploading the image
            timestamp = System.currentTimeMillis(),
            likes = 0,
            comments = mutableMapOf()
        )
        val databaseReference = FirebaseDatabase.getInstance().reference.child("posts").child(post.id)
        databaseReference.setValue(post).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Post shared successfully", Toast.LENGTH_SHORT).show()
                postEditText.text.clear()
            } else {
                Toast.makeText(this, "Failed to share post: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Test if the image can be uploaded but i failed
    private fun uploadTestImage() {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4 // Adjust this value to control the downsampling (higher value = smaller bitmap)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_image, options)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        val storageReference = FirebaseStorage.getInstance().reference.child("test_images/${UUID.randomUUID()}.png")
        val uploadTask = storageReference.putBytes(data)
        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                Toast.makeText(this, "Test image uploaded: $downloadUri", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload test image: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadComment(postId: String, commentContent: String) {
        val comment = Post.Comment(
            userId = "user_id",
            content = commentContent,
            timestamp = System.currentTimeMillis()
        )

        val databaseReference = FirebaseDatabase.getInstance().reference
            .child("posts")
            .child(postId)
            .child("comments")
            .push()

        databaseReference.setValue(comment).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add comment: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun setupBottomNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_new_post // Highlight the new post button
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                R.id.navigation_group -> {
                    val intent = Intent(this, GroupsActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                R.id.navigation_new_post -> {
                    // We're already in NewPostActivity, so no need to start it again.
                    true
                }
                R.id.navigation_news_and_update -> {
                    val intent = Intent(this, NewsAndUpdatesActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra("userData", currentUser)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
