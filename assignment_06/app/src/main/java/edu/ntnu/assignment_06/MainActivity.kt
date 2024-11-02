package edu.ntnu.assignment_06

import ChatClient
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var chatServer: ChatServer
    private lateinit var chatClient: ChatClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        chatServer = ChatServer(textView)
        chatClient = ChatClient(textView)

        chatServer.startServer()
        chatClient.connectToServer()

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotBlank()) {
                chatClient.sendMessage(message)
                messageInput.text.clear()
            }
        }
    }
}
