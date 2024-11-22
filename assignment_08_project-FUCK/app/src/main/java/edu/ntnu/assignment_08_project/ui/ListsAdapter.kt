package edu.ntnu.assignment_08_project.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ntnu.assignment_08_project.R

class ListsAdapter(
  private val listFiles: MutableList<String>,
  private val onClick: (fileName: String) -> Unit
) : RecyclerView.Adapter<ListsAdapter.ListViewHolder>() {

  inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvListName: TextView = itemView.findViewById(R.id.tv_list_name)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_list_overview, parent, false)
    return ListViewHolder(view)
  }

  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    val fileName = listFiles[position]
    holder.tvListName.text = fileName.removeSuffix(".json") // Fjern filendelse
    holder.itemView.setOnClickListener { onClick(fileName) }
  }

  override fun getItemCount(): Int = listFiles.size

  fun addList(fileName: String) {
    listFiles.add(fileName)
    notifyItemInserted(listFiles.size - 1)
  }
}