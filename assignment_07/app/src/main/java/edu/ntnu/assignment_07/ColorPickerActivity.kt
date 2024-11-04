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

        findViewById<Button>(R.id.button_dark_mode).setOnClickListener {
            setBackgroundColor("#333333") // Mørkegrå bakgrunn for mørk modus som ikke er ikke er billigvarianten med "vi bare inverterte fargene"
        }

        findViewById<Button>(R.id.button_hackerman).setOnClickListener {
            setBackgroundColor("#000000") // Svart bakgrunn for Hackerman modus
        }

        findViewById<Button>(R.id.button_light_mode).setOnClickListener {
            setBackgroundColor("#FFFFFF") // Hvit bakgrunn for lys/default modus
        }

        // Tilbakeknapp for å gå tilbake til hovedskjermen
        findViewById<Button>(R.id.button_back).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Avslutt aktiviteten fjerne den fra tilbake-stacken
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
