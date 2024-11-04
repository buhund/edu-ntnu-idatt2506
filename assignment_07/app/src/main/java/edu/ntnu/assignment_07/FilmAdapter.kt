package edu.ntnu.assignment_07

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmAdapter(private val films: List<Film>, private val textColor: Int) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.textTitle)
        val directorTextView: TextView = view.findViewById(R.id.textDirector)
        val actorsTextView: TextView = view.findViewById(R.id.textActors)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        holder.titleTextView.text = film.title
        holder.directorTextView.text = "Director: ${film.director}"
        holder.actorsTextView.text = "Actors: ${film.actors.joinToString(", ")}"

        // Sett skriftfarge basert på valgt farge
        holder.titleTextView.setTextColor(textColor)
        holder.directorTextView.setTextColor(textColor)
        holder.actorsTextView.setTextColor(textColor)
    }

    override fun getItemCount(): Int = films.size
}
