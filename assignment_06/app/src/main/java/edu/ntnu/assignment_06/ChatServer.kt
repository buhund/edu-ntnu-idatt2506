package edu.ntnu.assignment_06

import android.widget.TextView
import kotlinx.coroutines.*
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList

class ChatServer(private val textView: TextView, private val serverUsername: String) {
    private val serverPort = 12345
    private lateinit var serverSocket: ServerSocket
    private val clientSockets = CopyOnWriteArrayList<Socket>()

    fun startServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverSocket = ServerSocket(serverPort)
                textView.append("Server ($serverUsername) startet på port $serverPort\n")
                while (true) {
                    val clientSocket = serverSocket.accept()
                    clientSockets.add(clientSocket)
                    textView.append("Klient tilkoblet: ${clientSocket.inetAddress.hostAddress}\n")
                    handleClient(clientSocket)
                }
            } catch (e: Exception) {
                textView.append("Feil i server: ${e.message}\n")
                e.printStackTrace()
            }
        }
    }

    private fun handleClient(clientSocket: Socket) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reader = clientSocket.getInputStream().bufferedReader()
                while (true) {
                    val message = reader.readLine() ?: break
                    textView.append("$message\n")
                    sendMessageToClients(message)
                }
            } catch (e: Exception) {
                textView.append("Feil ved klienthåndtering: ${e.message}\n")
                e.printStackTrace()
            } finally {
                clientSockets.remove(clientSocket)
                clientSocket.close()
                textView.append("Klient frakoblet\n")
            }
        }
    }

    fun sendMessageToClients(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            for (clientSocket in clientSockets) {
                try {
                    val writer = PrintWriter(clientSocket.getOutputStream(), true)
                    writer.println("$serverUsername (Server): $message")
                } catch (e: Exception) {
                    textView.append("Feil ved sending til klient: ${e.message}\n")
                    e.printStackTrace()
                }
            }
            textView.append("Sendt fra $serverUsername: $message\n")
        }
    }
}
