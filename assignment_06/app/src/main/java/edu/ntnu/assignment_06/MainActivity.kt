package edu.ntnu.assignment_06

import ChatClient
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var chatServer: ChatServer
    private lateinit var chatClient: ChatClient
    private var isServerInstance: Boolean? = null
    private var username: String = "Bruker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        askForUsername(textView)

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotBlank()) {
                if (isServerInstance == true) {
                    chatServer.sendMessageToClients(message)
                } else {
                    chatClient.sendMessage(message)
                }
                messageInput.text.clear()
            }
        }
    }

    private fun askForUsername(textView: TextView) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Velg brukernavn")
        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            username = input.text.toString().ifBlank { "Bruker" }
            showRoleSelectionDialog(textView)
        }
        builder.setCancelable(false)
        builder.show()
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
        chatServer = ChatServer(textView, username)
        chatServer.startServer()
        Log.d("MainActivity", "Server startet som $username.")
        textView.append("Server startet som $username.\n")
    }

    private fun connectAsClient(textView: TextView) {
        chatClient = ChatClient(textView, username)
        chatClient.connectToServer()
    }
}
