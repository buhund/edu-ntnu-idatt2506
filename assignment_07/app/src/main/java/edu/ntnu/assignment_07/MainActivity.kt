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
    private lateinit var fileWriterHelper: FileWriterHelper
    private lateinit var recyclerView: RecyclerView
    private var textColor: Int = Color.BLACK // Standard skriftfarge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser SharedPreferences for bakgrunnsfarge og skriftfarge
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val backgroundColor = sharedPreferences.getString("backgroundColor", "#FFFFFF") // Standard: hvit bakgrunn

        // Sett skriftfarge basert på bakgrunnsfarge
        textColor = when (backgroundColor) {
            "#333333" -> Color.WHITE   // Mørk modus: hvit skrift
            "#000000" -> Color.GREEN   // Hackerman: grønn tekst
            "#FFFFFF" -> Color.BLACK   // Lys modus: svart tekst
            else -> Color.BLACK        // Standardfarge (svart tekst)
        }

        // Sett bakgrunnsfarge på RecyclerView/hele layouten
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setBackgroundColor(Color.parseColor(backgroundColor))

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        databaseManager = DatabaseManager(this)
        fileWriterHelper = FileWriterHelper(this) // Initialiserer fileWriterHelper
        recyclerView = findViewById(R.id.recyclerView)

        // Tøm databasen før innsetting
        databaseManager.clearDatabase()

        // Les filmer fra fil og sett dem inn i databasen
        val fileReaderHelper = FileReaderHelper(this)
        val films = fileReaderHelper.parseFilmsFromFile(R.raw.films)
        databaseManager.insertFilms(films)

        // Skriv filmene til en lokal fil
        fileWriterHelper.writeFilmsToFile(films, "saved_films.txt")

        recyclerView.layoutManager = LinearLayoutManager(this)
        displayAllFilms()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_all -> {
                displayAllFilms()
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

        val alertDialog = builder.create()
        backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showActorSelectionDialog() {
        val actors = databaseManager.getUniqueActors()
        val builder = AlertDialog.Builder(this)

        val customTitleView = LayoutInflater.from(this).inflate(R.layout.dialog_title, null)
        val backButton = customTitleView.findViewById<Button>(R.id.button_back)
        val titleTextView = customTitleView.findViewById<TextView>(R.id.dialog_title_text)

        titleTextView.text = "Velg skuespiller"

        builder.setCustomTitle(customTitleView)
        builder.setItems(actors.toTypedArray()) { dialog, which ->
            val selectedActor = actors[which]
            displayFilmsByActor(selectedActor)
            Toast.makeText(this, "Viser filmer med $selectedActor", Toast.LENGTH_SHORT).show()
        }

        val alertDialog = builder.create()
        backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun displayAllFilms() {
        val films = databaseManager.getAllFilms()
        recyclerView.adapter = FilmAdapter(films, textColor)
    }

    private fun displayFilmsByDirector(director: String) {
        val films = databaseManager.getFilmsByDirector(director)
        recyclerView.adapter = FilmAdapter(films, textColor)
    }

    private fun displayFilmsByActor(actor: String) {
        val films = databaseManager.getFilmsByActor(actor)
        recyclerView.adapter = FilmAdapter(films, textColor)
    }
}
