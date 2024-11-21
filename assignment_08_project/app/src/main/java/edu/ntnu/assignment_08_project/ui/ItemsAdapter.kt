package edu.ntnu.assignment_08_project.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ntnu.assignment_08_project.R
import edu.ntnu.assignment_08_project.data.models.ListItem

class ItemsAdapter(private val items: MutableList<ListItem>, // Mutable ist of items
                   private val onDeleteClick: (position: Int) -> Unit, // Callback for delete
                   private val onItemStatusChanged: () -> Unit // Callback for status change
) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
        val buttonDeleteItem: Button = itemView.findViewById(R.id.button_delete_item)
        val ivCheckmark: ImageView = itemView.findViewById(R.id.iv_checkmark) // Add this
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.tvItemName.text = item.name

        // Set initial visibility of the checkmark
        holder.ivCheckmark.visibility = if (item.isDone) View.VISIBLE else View.GONE

        // Change style based on status
        holder.tvItemName.alpha = if (item.isDone) 0.5f else 1.0f

        // Delete-button click listener
        holder.buttonDeleteItem.setOnClickListener {
            onDeleteClick(position)
        }

        // Mark as Done/Not Done on click
        holder.itemView.setOnClickListener {
            item.isDone = !item.isDone
            items.removeAt(position)
            notifyItemRemoved(position)

            // Move element to bottom if done, otherwise to the top
            val newPosition = if (item.isDone) items.size else 0
            items.add(newPosition, item)
            notifyItemInserted(newPosition)

            // Update checkmark visibility
            holder.ivCheckmark.visibility = if (item.isDone) View.VISIBLE else View.GONE

            // Callback to update JSON file
            onItemStatusChanged()
        }
    }


    override fun getItemCount(): Int = items.size
}






















