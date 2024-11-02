package edu.ntnu.exercise03

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(
    private val friends: List<Friend>,
    private val onFriendClick: (Friend, Int) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
        holder.itemView.setOnClickListener {
            onFriendClick(friend, position)
        }
    }

    override fun getItemCount(): Int = friends.size

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.friend_name)
        private val birthdateTextView: TextView = itemView.findViewById(R.id.friend_birthdate)

        fun bind(friend: Friend) {
            nameTextView.text = friend.name
            birthdateTextView.text = friend.birthdate
        }
    }
}
