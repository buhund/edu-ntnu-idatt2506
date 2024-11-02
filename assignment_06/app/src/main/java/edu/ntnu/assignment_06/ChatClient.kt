import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ChatClient(private val textView: TextView, private val clientUsername: String) {
    private val serverIp = "10.0.2.2"
    private val serverPort = 12345
    private lateinit var clientSocket: Socket
    private lateinit var writer: PrintWriter

    fun connectToServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                clientSocket = Socket(serverIp, serverPort)
                writer = PrintWriter(clientSocket.getOutputStream(), true)
                textView.append("$clientUsername tilkoblet server\n")
                readMessagesFromServer()
            } catch (e: Exception) {
                textView.append("Tilkobling feilet: ${e.message}\n")
                e.printStackTrace()
            }
        }
    }

    private suspend fun readMessagesFromServer() {
        withContext(Dispatchers.IO) {
            try {
                val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                var message: String?
                while (true) {
                    message = reader.readLine() ?: break
                    withContext(Dispatchers.Main) {
                        textView.append("$message\n")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    textView.append("Feil ved mottak fra server: ${e.message}\n")
                }
                e.printStackTrace()
            } finally {
                clientSocket.close()
                withContext(Dispatchers.Main) {
                    textView.append("$clientUsername frakoblet\n")
                }
            }
        }
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                writer.println("$clientUsername: $message")
                withContext(Dispatchers.Main) {
                    textView.append("Sendt fra $clientUsername: $message\n")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    textView.append("Feil ved sending til server: ${e.message}\n")
                }
                e.printStackTrace()
            }
        }
    }
}
