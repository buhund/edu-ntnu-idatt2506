package edu.ntnu.assignment_07

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var databaseManager: DatabaseManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hent lagret bakgrunnsfarge fra SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val backgroundColor = sharedPreferences.getString("backgroundColor", "#FFFFFF") // Standard: hvit bakgrunn

        // Sett bakgrunnsfarge på RecyclerView eller hele layouten
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setBackgroundColor(Color.parseColor(backgroundColor))

        // Bestem skriftfarge basert på bakgrunnsfargen
        val textColor = when (backgroundColor) {
            "#333333" -> Color.WHITE   // Mørk modus: hvit skrift
            "#000000" -> Color.GREEN   // Hackerman: grønn tekst
            "#FFFFFF" -> Color.BLACK   // Lys modus: svart tekst
            else -> Color.BLACK        // Standardfarge
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        databaseManager = DatabaseManager(this)
        recyclerView = findViewById(R.id.recyclerView)

        // Tøm databasen før innsetting
        databaseManager.clearDatabase()

        // Les filmer fra fil og sett dem inn i databasen
        val fileReaderHelper = FileReaderHelper(this)
        val films = fileReaderHelper.parseFilmsFromFile(R.raw.films)
        databaseManager.insertFilms(films)

        // Sett opp RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        displayAllFilms(textColor)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)  // Legger til menyen i verktøylinjen
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_all -> {
                displayAllFilms(Color.BLACK) // Bruk standard skriftfarge
                Toast.makeText(this, "Viser alle filmer", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.filter_by_director -> {
                showDirectorSelectionDialog()
                return true
            }
            R.id.filter_by_actor -> {
                showActorSelectionDialog()
                return true
            }
            R.id.select_background_color -> {
                // Naviger til ColorPickerActivity
                val intent = Intent(this, ColorPickerActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDirectorSelectionDialog() {
        val directors = databaseManager.getUniqueDirectors()
        val builder = AlertDialog.Builder(this)

        // Opprett en tilpasset layout for dialogens tittel med en tilbake-knapp
        val customTitleView = LayoutInflater.from(this).inflate(R.layout.dialog_title, null)
        val backButton = customTitleView.findViewById<Button>(R.id.button_back)
        val titleTextView = customTitleView.findViewById<TextView>(R.id.dialog_title_text)

        titleTextView.text = "Velg regissør"

        builder.setCustomTitle(customTitleView)
        builder.setItems(directors.toTypedArray()) { dialog, which ->
            val selectedDirector = directors[which]
            displayFilmsByDirector(selectedDirector)
            Toast.makeText(this, "Viser filmer av $selectedDirector", Toast.LENGTH_SHORT).show()
        }

        // Opprett og vis dialog
        val alertDialog = builder.create()

        // Sett opp tilbake-knapp til å lukke dialogen
        backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showActorSelectionDialog() {
        val actors = databaseManager.getUniqueActors()
        val builder = AlertDialog.Builder(this)

        // Opprett en tilpasset layout for dialogens tittel med en tilbake-knapp
        val customTitleView = LayoutInflater.from(this).inflate(R.layout.dialog_title, null)
        val backButton = customTitleView.findViewById<Button>(R.id.button_back)
        val titleTextView = customTitleView.findViewById<TextView>(R.id.dialog_title_text)

        // Sett riktig tekst for skuespillerdialogen
        titleTextView.text = "Velg skuespiller"

        builder.setCustomTitle(customTitleView)
        builder.setItems(actors.toTypedArray()) { dialog, which ->
            val selectedActor = actors[which]
            displayFilmsByActor(selectedActor)
            Toast.makeText(this, "Viser filmer med $selectedActor", Toast.LENGTH_SHORT).show()
        }

        // Opprett og vis dialogen
        val alertDialog = builder.create()

        // Sett opp tilbake-knappen til å lukke dialogen
        backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun displayAllFilms(textColor: Int) {
        val films = databaseManager.getAllFilms()
        recyclerView.adapter = FilmAdapter(films, textColor)
    }

    private fun displayFilmsByDirector(director: String) {
        val films = databaseManager.getFilmsByDirector(director)
        recyclerView.adapter = FilmAdapter(films, Color.BLACK)
    }

    private fun displayFilmsByActor(actor: String) {
        val films = databaseManager.getFilmsByActor(actor)
        recyclerView.adapter = FilmAdapter(films, Color.BLACK)
    }
}
