import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ChatClient(private val textView: TextView, private val serverIp: String = "10.0.2.2", private val port: Int = 12345) {
  private var socket: Socket? = null
  private val coroutineScope = CoroutineScope(Dispatchers.IO)

  fun connectToServer() {
    coroutineScope.launch {
      try {
        socket = Socket(serverIp, port)
        updateUI("Connected to server")

        listenForMessages()
      } catch (e: Exception) {
        updateUI("Connection error: ${e.message}")
      }
    }
  }

  fun sendMessage(message: String) {
    coroutineScope.launch {
      socket?.let {
        PrintWriter(it.getOutputStream(), true).println(message)
        updateUI("Sent: $message")
      }
    }
  }

  private fun listenForMessages() {
    coroutineScope.launch {
      val reader = BufferedReader(InputStreamReader(socket?.getInputStream()))

      try {
        while (true) {
          val message = reader?.readLine() ?: break
          updateUI("Received: $message")
        }
      } finally {
        socket?.close()
        updateUI("Disconnected from server")
      }
    }
  }

  private fun updateUI(text: String) {
    MainScope().launch {
      textView.append("$text\n")
    }
  }
}