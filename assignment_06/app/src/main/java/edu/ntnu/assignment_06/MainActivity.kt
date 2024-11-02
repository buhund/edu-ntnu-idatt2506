package edu.ntnu.assignment_06

import ChatClient
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var chatServer: ChatServer
    private lateinit var chatClient: ChatClient
    private var isServerInstance: Boolean? = null  // Holder valgt rolle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        // Vis dialog for å velge rolle
        showRoleSelectionDialog(textView)

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotBlank() && isServerInstance == false) {
                chatClient.sendMessage(message)
                messageInput.text.clear()
            }
        }
    }

    private fun showRoleSelectionDialog(textView: TextView) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Velg Rolle")
        builder.setMessage("Vil du starte som server eller klient?")
        builder.setPositiveButton("Server") { _, _ ->
            isServerInstance = true
            startServer(textView)
        }
        builder.setNegativeButton("Klient") { _, _ ->
            isServerInstance = false
            connectAsClient(textView)
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun startServer(textView: TextView) {
        chatServer = ChatServer(textView)
        chatServer.startServer()
        Log.d("MainActivity", "Server startet.")
        textView.append("Server startet.\n")
    }

    private fun connectAsClient(textView: TextView) {
        chatClient = ChatClient(textView)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("MainActivity", "Prøver å koble til som klient.")
                chatClient.connectToServer()
                Log.d("MainActivity", "Koblet til som klient.")
                withContext(Dispatchers.Main) {
                    textView.append("Koblet til som klient.\n")
                }
            } catch (e: Exception) {
                Log.d("MainActivity", "Tilkobling feilet som klient: ${e.message}")
                withContext(Dispatchers.Main) {
                    textView.append("Tilkobling feilet som klient.\n")
                }
            }
        }
    }
}