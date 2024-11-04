/**
 * src/main/java/edu/ntnu/assignment_07/MainActivity.kt
 */

package edu.ntnu.assignment_07

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var databaseManager: DatabaseManager
    private lateinit var fileWriterHelper: FileWriterHelper
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseManager = DatabaseManager(this)
        recyclerView = findViewById(R.id.recyclerView)

        fileWriterHelper = FileWriterHelper(this)
        val fileReaderHelper = FileReaderHelper(this)

        // Tøm databasen før innsetting
        databaseManager.clearDatabase()

        // Les filmer fra fil og sett dem inn i databasen
        val films = fileReaderHelper.parseFilmsFromFile(R.raw.films)
        databaseManager.insertFilms(films)

        // Skriv filmer til en ny lokal fil
        fileWriterHelper.writeFilmsToFile(films, "saved_films.txt")

        // Debug toast muffin
        Toast.makeText(this, "Data has been loaded successfully!", Toast.LENGTH_SHORT).show()

        // Hent filmer fra databasen
        val filmsFromDatabase = databaseManager.getAllFilms()

        // Sett opp RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FilmAdapter(filmsFromDatabase)
    }



}


// End of class MainActivity