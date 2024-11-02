package edu.ntnu.assignment_06

import android.widget.TextView
import java.net.ServerSocket
import java.net.Socket
import kotlinx.coroutines.*

class ChatServer(private val textView: TextView, private val port: Int = 12345) {
    private val clients = mutableListOf<Socket>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun startServer() {
        coroutineScope.launch {
            try {
                val serverSocket = ServerSocket(port)
                setUI("Server started on port: $port")

                while (true) {
                    val clientSocket = serverSocket.accept()
                    clients.add(clientSocket)
                    setUI("Client connected: ${clientSocket.inetAddress.hostAddress}")

                    handleClient(clientSocket)
                }
            } catch (e: Exception) {
                setUI("Server error: ${e.message}")
            }
        }
    }

    private fun handleClient(socket: Socket) {
        coroutineScope.launch {
            val reader = socket.getInputStream().bufferedReader()

            try {
                while (true) {
                    val message = reader.readLine() ?: break
                    broadcastMessage("Client ${socket.inetAddress.hostAddress}: $message")
                }
            } finally {
                socket.close()
                clients.remove(socket)
                setUI("Client disconnected: ${socket.inetAddress.hostAddress}")
            }
        }
    }

    private fun broadcastMessage(message: String) {
        for (client in clients) {
            client.getOutputStream().write((message + "\n").toByteArray())
        }
        setUI("Broadcasted: $message")
    }

    private fun setUI(text: String) {
        MainScope().launch {
            textView.append("$text\n")
        }
    }
}
