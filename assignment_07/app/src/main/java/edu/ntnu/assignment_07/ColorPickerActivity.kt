package edu.ntnu.assignment_07

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ColorPickerActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Knapper for tema-valg
        findViewById<Button>(R.id.button_dark_mode).setOnClickListener {
            setBackgroundColor("#333333") // Mørkegrå for mørk modus
        }

        findViewById<Button>(R.id.button_hackerman).setOnClickListener {
            setBackgroundColor("#000000") // Svart for hackerman modus
        }

        findViewById<Button>(R.id.button_light_mode).setOnClickListener {
            setBackgroundColor("#FFFFFF") // Hvit for lys modus
        }

        // Tilbakeknapp for å gå tilbake til hovedskjermen
        findViewById<Button>(R.id.button_back).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Avslutt denne aktiviteten for å unngå å ha den i tilbake-stakken
        }
    }

    private fun setBackgroundColor(color: String) {
        val editor = sharedPreferences.edit()
        editor.putString("backgroundColor", color)
        editor.apply()

        // Gå tilbake til MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
