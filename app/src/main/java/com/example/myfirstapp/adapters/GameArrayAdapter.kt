package com.example.myfirstapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.myfirstapp.R
import com.example.myfirstapp.data.Game

class GameArrayAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val gamesList: List<Game>
) : ArrayAdapter<Game>(context, layoutResource, gamesList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)

        val gameNameTextView: TextView = view.findViewById(R.id.textViewGameName)
        val gameImage : ImageView =view.findViewById(R.id.imageViewGame);
        val game = gamesList[position]
        gameNameTextView.text = game.name
        gameImage.setImageResource(game.imageResourceId)

        return view
    }
}