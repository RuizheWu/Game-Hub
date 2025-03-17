package com.example.myfirstapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.data.User
import com.google.firebase.database.FirebaseDatabase
import android.widget.PopupMenu
import android.view.View

class EditUserProfileActivity : AppCompatActivity() {
    private lateinit var currentUser: User
    private lateinit var gamePreferenceTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)
        currentUser = intent.getParcelableExtra("userData") ?: User()

        val profileName: EditText = findViewById(R.id.edit_user_profile_name)
        val aboutMe: EditText = findViewById(R.id.edit_user_about_me)
        gamePreferenceTextView = findViewById(R.id.edit_user_gaming_preferences)
        val userAvatar: ImageView = findViewById(R.id.edit_user_avatar)

        profileName.hint = currentUser.username
        aboutMe.hint = currentUser.description

        //set user gaming preference
        val tempGamePreference : String = currentUser.interest
        updateGamePreferenceText()
        val gamePreferenceButton: Button = findViewById(R.id.game_preference_button)
        gamePreferenceButton.setOnClickListener {
            showGamePreferenceDropDown(gamePreferenceButton)
        }
        //empty all text in the gaming preference when empty button is clicked
        val emptyPreferenceButton: Button = findViewById(R.id.empty_preference_button)
        emptyPreferenceButton.setOnClickListener {
            gamePreferenceTextView.text = ""
            currentUser.interest = ""
            updateFirebase("interest", currentUser.interest)
        }
        //delete a user preference
        val deletePreferenceButton:Button = findViewById(R.id.delete_preference_button)
        deletePreferenceButton.setOnClickListener {
            showDeletePreferenceDialog()
        }

        //display user Avatar
        userAvatar.setImageResource(currentUser.avatarId)

        //monitor save change button
        val saveButton: Button = findViewById(R.id.save_profile_button)
        saveButton.setOnClickListener {
            //update username and unlock the achievement if is edited
            val username = profileName.text.toString()
            if (username.isNotEmpty() && username != currentUser.uid) {
                currentUser.username = username
                if (!currentUser.achievements[1].unlocked) {
                    currentUser.achievements[1].unlocked = true

                    unlockAchievement(currentUser.achievements[1].name)
                }

                //update firebase
                updateFirebase("username", currentUser.username)
            }

            //update about me and unlock the achievement if is edited
            val newAboutMe = aboutMe.text.toString()
            if (newAboutMe.isNotEmpty() && newAboutMe != "None") {
                currentUser.description = newAboutMe
                if (!currentUser.achievements[2].unlocked) {
                    currentUser.achievements[2].unlocked = true

                    unlockAchievement(currentUser.achievements[2].name)
                }

                //update firebase
                updateFirebase("description", currentUser.description)
            }

            //update gaming preference
            if (currentUser.interest != tempGamePreference) {
                updateFirebase("interest", currentUser.interest)

                if (!currentUser.achievements[3].unlocked) {
                    currentUser.achievements[3].unlocked = true

                    unlockAchievement(currentUser.achievements[3].name)
                }
            }

            //update uer avatar
            val database = FirebaseDatabase.getInstance().getReference("Registered_Users").child(currentUser.uid)
            database.child("avatarId").setValue(currentUser.avatarId)
                .addOnCompleteListener { databaseTask ->
                    if (databaseTask.isSuccessful) {
                        Toast.makeText(this, "Avatar updated!", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "Failed to update Avatar", Toast.LENGTH_SHORT).show()
                    }
                }


            //finished updating, back to user profile
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("userData", currentUser)
            startActivity(intent)
        }

        //change user avatar
        userAvatar.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_choose_avatar, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()

            dialogView.findViewById<ImageView>(R.id.avatar_one_image).setOnClickListener {
                // Set the user's avatar to avatar_one and dismiss the dialog
                userAvatar.setImageResource(R.drawable.avatar_one)
                currentUser.avatarId = R.drawable.avatar_one
                dialog.dismiss()
            }

            dialogView.findViewById<ImageView>(R.id.avatar_two_image).setOnClickListener {
                // Set the user's avatar to avatar_two and dismiss the dialog
                userAvatar.setImageResource(R.drawable.avatar_two)
                currentUser.avatarId = R.drawable.avatar_two
                dialog.dismiss()
            }

            dialogView.findViewById<ImageView>(R.id.avatar_three_image).setOnClickListener {
                userAvatar.setImageResource(R.drawable.avatar_three)
                currentUser.avatarId = R.drawable.avatar_three
                dialog.dismiss()
            }

            dialogView.findViewById<ImageView>(R.id.avatar_four_image).setOnClickListener {
                userAvatar.setImageResource(R.drawable.avatar_four)
                currentUser.avatarId = R.drawable.avatar_four
                dialog.dismiss()
            }

            dialogView.findViewById<ImageView>(R.id.avatar_five_image).setOnClickListener {
                userAvatar.setImageResource(R.drawable.avatar_five)
                currentUser.avatarId = R.drawable.avatar_five
                dialog.dismiss()
            }

            dialogView.findViewById<ImageView>(R.id.avatar_six_image).setOnClickListener {
                userAvatar.setImageResource(R.drawable.avatar_six)
                currentUser.avatarId = R.drawable.avatar_six
                dialog.dismiss()
            }

            dialog.show()
        }


    }
    private fun addGamePreference(preference: String) {
        val currentPreference = currentUser.interest
        if (!currentPreference.contains(preference)) {
            currentUser.interest = if (currentPreference.isNotEmpty()) {
                "$currentPreference, $preference"
            } else {
                preference
            }
            updateGamePreferenceText()
        } else {
            Toast.makeText(this, "Preference already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFirebase(key: String, value: String) {
        val database = FirebaseDatabase.getInstance().getReference("Registered_Users").child(currentUser.uid)
        database.child(key).setValue(value)
            .addOnCompleteListener { databaseTask ->
                if (databaseTask.isSuccessful) {
                    Toast.makeText(this, "$key updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update $key", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun unlockAchievement(achievementName: String) {
        val database = FirebaseDatabase.getInstance().getReference("Registered_Users").child(currentUser.uid)
        database.child("achievements").setValue(currentUser.achievements)
            .addOnCompleteListener { databaseTask ->
                if (databaseTask.isSuccessful) {
                    Toast.makeText(this, "'$achievementName' unlocked!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to unlock achievement", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //show available preferences for the user
    private fun showGamePreferenceDropDown(anchorView: View) {
        val gamePreferences = arrayOf("Action", "Adventure", "Racing", "Strategy", "Role-Playing", "Sports", "FPS", "Multiple Payer", "Single Player", "Adult")

        val popupMenu = PopupMenu(this, anchorView)
        for (gamePreference in gamePreferences) {
            popupMenu.menu.add(gamePreference)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedPreference = menuItem.title.toString()
            addGamePreference(selectedPreference)
            true
        }


        popupMenu.show()
    }

    private fun updateGamePreferenceText() {
        gamePreferenceTextView.text = currentUser.interest
    }

    //Show the Dialog that contain the preferences can be deleted
    private fun showDeletePreferenceDialog() {
        val gamePreferences = currentUser.interest.split(", ")
        val preferencesArray = gamePreferences.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Select a preference to delete")
            .setItems(preferencesArray) { dialog, which ->

                val selectedPreference = preferencesArray[which]
                currentUser.interest = gamePreferences.filter { it != selectedPreference }.joinToString(", ")

                updateGamePreferenceText()

                // Update Firebase
                updateFirebase("interest", currentUser.interest)

                dialog.dismiss()
            }
            .show()
    }
}