/**
 * src/main/java/edu/ntnu/assignment_07/FileReaderHelper.kt
 * Hjelpeklasse for å lese inn fra films.txt
 */

package edu.ntnu.assignment_07

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class FileReaderHelper(private val context: Context) {

    fun parseFilmsFromFile(fileId: Int): List<Film> {
        val films = mutableListOf<Film>()

        try {
            val inputStream = context.resources.openRawResource(fileId)
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Skip the header line
            reader.readLine()

            // Read and parse each line
            reader.forEachLine { line ->
                val parts = line.split(",").map { it.trim() }
                if (parts.size >= 4) {
                    val title = parts[0]
                    val director = parts[1]
                    // Bruker subList for å fjerne de to første elementene,
                    // og dermed få en liste med bare skuespillerene.
                    val actors = parts.subList(2, parts.size)
                    films.add(Film(title, director, actors))
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return films
    }
}

// End of class FileReaderHelper