package edu.ntnu.exercise02

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  private lateinit var number1TextView: TextView
  private lateinit var number2TextView: TextView
  private lateinit var answerEditText: EditText
  private lateinit var upperLimitEditText: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    number1TextView = findViewById(R.id.text_number1)
    number2TextView = findViewById(R.id.text_number2)
    answerEditText = findViewById(R.id.edit_answer)
    upperLimitEditText = findViewById(R.id.edit_upper_limit)

    val addButton: Button = findViewById(R.id.button_add)
    val multiplicationButton: Button = findViewById(R.id.button_multiplication)

    // Set onClick listeners for the buttons
    addButton.setOnClickListener {
      checkAnswer(isAddition = true)
    }

    multiplicationButton.setOnClickListener {
      checkAnswer(isAddition = false)
    }

    // Start by generating random numbers
    generateRandomNumbers()
  }

  // Method to check the users answer and perform addition or multiplication
  private fun checkAnswer(isAddition: Boolean) {
    val number1 = number1TextView.text.toString().toInt()
    val number2 = number2TextView.text.toString().toInt()
    val userAnswer = answerEditText.text.toString().toInt()

    val correctAnswer = if (isAddition) number1 + number2 else number1 * number2

    if (userAnswer == correctAnswer) {
      Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()
    } else {
      val wrongAnswerMessage = getString(R.string.wrong, correctAnswer.toString())
      Toast.makeText(this, wrongAnswerMessage, Toast.LENGTH_SHORT).show()
    }

    // Generate new random numbers after each operation
    generateRandomNumbers()
  }

  // Method to generate new random numbers based on the upper limit
  private fun generateRandomNumbers() {
    val upperLimit = upperLimitEditText.text.toString().toIntOrNull() ?: 100 // Default to 100

    // Generate two random numbers within the upper limit
    val randomNumber1 = (0..upperLimit).random()
    val randomNumber2 = (0..upperLimit).random()

    // Update the TextViews with the new random numbers
    number1TextView.text = randomNumber1.toString()
    number2TextView.text = randomNumber2.toString()
  }
}