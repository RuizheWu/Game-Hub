package com.example.myfirstapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.Achievement

class AchievementAdapter(private val achievements: List<Achievement>): RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {
    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val unlockedTextView: TextView = itemView.findViewById(R.id.unlockedTextView)
        private val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)

        fun bind(achievement: Achievement) {
            nameTextView.text = achievement.name
            descriptionTextView.text = achievement.description
            unlockedTextView.text = if (achievement.unlocked) "Unlocked" else "Locked"
            // Load and set the achievement icon
            iconImageView.setImageResource(achievement.iconResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_achievement, parent, false)
        return AchievementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievements[position]
        holder.bind(achievement)
    }

    override fun getItemCount(): Int {
        return achievements.size
    }
}