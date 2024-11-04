/**
 * Class: src/edu/ntnu/assignment_07/FilmAdapter
 */
package edu.ntnu.assignment_07

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log


class FilmAdapter(private val films: List<Film>) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val directorTextView: TextView = itemView.findViewById(R.id.directorTextView)
        val actorsTextView: TextView = itemView.findViewById(R.id.actorsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.film_item, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        holder.titleTextView.text = film.title
        holder.directorTextView.text = holder.itemView.context.getString(R.string.director_text, film.director)
        holder.actorsTextView.text = holder.itemView.context.getString(R.string.actors_text, film.actors.joinToString(", "))
        //holder.directorTextView.text = "Regissør: ${film.director}"
        //holder.actorsTextView.text = "Skuespillere: ${film.actors.joinToString(", ")}"
    }


    override fun getItemCount(): Int {
        Log.d("FilmAdapter", "Antall elementer: ${films.size}")
        return films.size
    }


}

