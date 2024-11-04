/**
 * Class: src/edu/ntnu/assignment_07/MainActivity
 */
package edu.ntnu.assignment_07

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var databaseManager: DatabaseManager
    private lateinit var filmAdapter: FilmAdapter
    private lateinit var resultView: TextView
    private var allFilms: List<Film> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Assignment 07"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseManager = DatabaseManager(this)

        // Laste filmer fra `films.txt` til databasen
        importFilmsFromFile()

        // Eksportere filmene fra databasen til en ny fil
        databaseManager.exportFilmsToFile(this, "exported_films.txt")

        // Logge for å bekrefte eksport
        Log.d("MainActivity", "Filmer er eksportert til exported_films.txt")

        // Hente og vise filmene
        allFilms = databaseManager.fetchAllFilms()
        Log.d("MainActivity", "Hentede filmer: $allFilms")
        filmAdapter = FilmAdapter(allFilms)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = filmAdapter
    }

    private fun importFilmsFromFile() {
        val inputStream = resources.openRawResource(R.raw.films)
        val films = mutableListOf<Film>()

        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",").map { it.trim() }
                if (parts.size >= 3) {
                    val title = parts[0]
                    val director = parts[1]
                    val actors = parts.subList(2, parts.size)
                    films.add(Film(title, director, actors))
                }
            }
        }

        databaseManager.insertFilms(films)
        Log.d("MainActivity", "Films imported from file: ${films.size}")
    }

    private fun filterFilms(query: String?) {
        val filteredFilms = if (!query.isNullOrEmpty()) {
            allFilms.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.director.contains(query, ignoreCase = true) ||
                        it.actors.any { actor -> actor.contains(query, ignoreCase = true) }
            }
        } else {
            allFilms
        }
        filmAdapter = FilmAdapter(filteredFilms)
        findViewById<RecyclerView>(R.id.recyclerView).adapter = filmAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.let {
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    filterFilms(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterFilms(newText)
                    return true
                }
            })
        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_all -> {
                showResults(allFilms.map { it.title })
                true
            }
            R.id.action_filter_director -> {
                showInputDialog("Skriv inn regissør") { director ->
                    showResults(databaseManager.getFilmsByDirector(director))
                }
                true
            }
            R.id.action_filter_actor -> {
                showInputDialog("Skriv inn skuespiller") { actor ->
                    showResults(databaseManager.getFilmsByActor(actor))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showResults(results: List<String>) {
        val resultText = StringBuilder()
        for (result in results) {
            resultText.append("$result\n")
        }
        resultView.text = resultText.toString()
    }

    private fun showInputDialog(title: String, onInputReceived: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        val inputField = EditText(this)
        inputField.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(inputField)

        builder.setPositiveButton("OK") { dialog, _ ->
            val inputText = inputField.text.toString().trim()
            if (inputText.isNotEmpty()) {
                onInputReceived(inputText)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Avbryt") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
