/**
 * src/main/java/edu/ntnu/assignment_07/FileWriterHelper.kt
 * Hjelpeklasse for å skrive filmene til en lokal fil, ihht oppgaveteksten.
 */

package edu.ntnu.assignment_07

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class FileWriterHelper(private val context: Context) {

    fun writeFilmsToFile(films: List<Film>, filename: String) {
        val file = File(context.filesDir, filename)

        try {
            FileOutputStream(file).use { outputStream ->
                films.forEach { film ->
                    val filmData = "Title: ${film.title}, Director: ${film.director}, Actors: ${film.actors.joinToString(", ")}\n"
                    outputStream.write(filmData.toByteArray())
                }
            }
            println("Films successfully written to $filename")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

// End of class FileWriterHelper