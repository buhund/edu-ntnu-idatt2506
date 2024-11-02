package edu.ntnu.assignment_05

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.*
import java.net.CookieManager
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    private lateinit var nameInput: EditText
    private lateinit var cardNumberInput: EditText
    private lateinit var startGameButton: Button
    private lateinit var clearDataButton: Button
    private lateinit var responseTextView: TextView
    private lateinit var guessInput: EditText
    private lateinit var submitGuessButton: Button
    private lateinit var guessResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing UI components
        nameInput = findViewById(R.id.nameInput)
        cardNumberInput = findViewById(R.id.cardNumberInput)
        startGameButton = findViewById(R.id.startGameButton)
        clearDataButton = findViewById(R.id.clearDataButton)
        responseTextView = findViewById(R.id.responseTextView)
        guessInput = findViewById(R.id.guessInput)
        submitGuessButton = findViewById(R.id.submitGuessButton)
        guessResultTextView = findViewById(R.id.guessResultTextView)

        // Enable cookies
        CookieManager.setDefault(CookieManager())

        // Start game button listener
        startGameButton.setOnClickListener {
            val name = nameInput.text.toString()
            val cardNumber = cardNumberInput.text.toString()
            if (name.isNotEmpty() && cardNumber.isNotEmpty()) {
                startGame(name, cardNumber)
            } else {
                responseTextView.text = "Fyll ut både navn og kortnummer."
            }
        }

        // Clear data button listener
        clearDataButton.setOnClickListener {
            clearGameData()
        }

        // Submit guess button listener
        submitGuessButton.setOnClickListener {
            val guess = guessInput.text.toString()
            if (guess.isNotEmpty()) {
                submitGuess(guess)
            } else {
                guessResultTextView.text = "Skriv inn et gyldig tall."
            }
        }
    }

    // Function to start game by sending name and card number
    private fun startGame(name: String, cardNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://bigdata.idi.ntnu.no/mobil/tallspill.jsp?navn=$name&kortnummer=$cardNumber")
            val response = sendRequest(url)
            withContext(Dispatchers.Main) {
                responseTextView.text = response
                if (response.contains("Oppgi et tall")) {
                    guessInput.isEnabled = true
                    submitGuessButton.isEnabled = true
                }
            }
        }
    }

    // Function to submit a guess
    private fun submitGuess(guess: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://bigdata.idi.ntnu.no/mobil/tallspill.jsp?tall=$guess")
            val response = sendRequest(url)
            withContext(Dispatchers.Main) {
                guessResultTextView.text = response
                Log.d("ServerResponse", response)
            }
        }
    }

    // Function to clear game data and reset UI
    private fun clearGameData() {
        // Clear user input fields
        nameInput.text.clear()
        cardNumberInput.text.clear()
        guessInput.text.clear()

        // Reset response and result text views
        responseTextView.text = "Response from server will appear here"
        guessResultTextView.text = "Result will appear here"

        // Disable guess-related input until a new game starts
        guessInput.isEnabled = false
        submitGuessButton.isEnabled = false

        Log.d("GameData", "User data and game state cleared.")
    }

    // Function to send HTTP request and handle cookies
    private fun sendRequest(url: URL): String {
        return try {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()
            response
        } catch (e: Exception) {
            Log.e("NetworkError", e.toString())
            "Network error: Unable to retrieve response"
        }
    }
}
