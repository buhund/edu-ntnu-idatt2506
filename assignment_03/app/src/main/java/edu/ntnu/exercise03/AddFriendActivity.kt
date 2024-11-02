package edu.ntnu.exercise03

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddFriendActivity : AppCompatActivity() {

  private var friendToEdit: Friend? = null
  private var friendPosition: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_friend)

    val nameInput = findViewById<EditText>(R.id.input_name)
    val datePicker = findViewById<DatePicker>(R.id.date_picker)
    val addButton = findViewById<Button>(R.id.button_add_friend)

    // Check if we are editing a friend, using deprecated functionality. Next, implement support for IE6.
    friendToEdit = intent.getSerializableExtra("friend_to_edit") as? Friend
    friendPosition = intent.getIntExtra("friend_position", -1)

    friendToEdit?.let {
      nameInput.setText(it.name)
      // Set DatePicker date
      // Example assuming birthdate is formatted as standard "day-month-year" format
      val parts = it.birthdate.split("-")
      datePicker.updateDate(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
    }

    addButton.text = if (friendToEdit == null) "Add Friend" else "Edit Friend"

    addButton.setOnClickListener {
      val name = nameInput.text.toString()
      val birthdate = "${datePicker.dayOfMonth}-${datePicker.month + 1}-${datePicker.year}"

      if (name.isNotBlank()) {
        val friend = Friend(name, birthdate)

        // If editing, return the edited friend
        val resultIntent = Intent()
        if (friendToEdit != null) {
          resultIntent.putExtra("edited_friend", friend)
          resultIntent.putExtra("friend_position", friendPosition)
          setResult(Activity.RESULT_OK, resultIntent)
        } else {
          resultIntent.putExtra("new_friend", friend)
          setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
      } else {
        nameInput.error = "Please enter a name"
      }
    }
  }
}

