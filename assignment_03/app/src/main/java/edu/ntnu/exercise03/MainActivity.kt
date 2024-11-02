package edu.ntnu.exercise03

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var friendsAdapter: FriendsAdapter
    private val friendsList = mutableListOf<Friend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.friends_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        friendsAdapter = FriendsAdapter(friendsList) { friend, position ->
            val intent = Intent(this, AddFriendActivity::class.java)
            intent.putExtra("friend_to_edit", friend)
            intent.putExtra("friend_position", position)
            startActivityForResult(intent, REQUEST_CODE_EDIT_FRIEND)
        }
        recyclerView.adapter = friendsAdapter

        val addFriendButton = findViewById<Button>(R.id.button_add_friend)
        addFriendButton.setOnClickListener {
            val intent = Intent(this, AddFriendActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_FRIEND)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_FRIEND && resultCode == Activity.RESULT_OK) {
            val newFriend = data?.getSerializableExtra("new_friend") as? Friend
            newFriend?.let {
                friendsList.add(it)
                friendsAdapter.notifyItemInserted(friendsList.size - 1)
            }
        } else if (requestCode == REQUEST_CODE_EDIT_FRIEND && resultCode == Activity.RESULT_OK) {
            val editedFriend = data?.getSerializableExtra("edited_friend") as? Friend
            val position = data?.getIntExtra("friend_position", -1)
            if (editedFriend != null && position != null && position != -1) {
                friendsList[position] = editedFriend
                friendsAdapter.notifyItemChanged(position)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_ADD_FRIEND = 1
        const val REQUEST_CODE_EDIT_FRIEND = 2
    }
}


