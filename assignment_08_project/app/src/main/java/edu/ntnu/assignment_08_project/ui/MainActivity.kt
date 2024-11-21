package edu.ntnu.assignment_08_project.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ntnu.assignment_08_project.R
import edu.ntnu.assignment_08_project.data.StorageManager
import edu.ntnu.assignment_08_project.data.models.ListData
import edu.ntnu.assignment_08_project.data.models.ListItem
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var storageManager: StorageManager
    private lateinit var listData: ListData
    private lateinit var rvItems: RecyclerView
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser StorageManager
        storageManager = StorageManager(this)

        // Last eller opprett liste
        val fileName = "groceries.json"
        listData = storageManager.readList(fileName) ?: ListData("Groceries", mutableListOf())


        // Sett opp RecyclerView
        rvItems = findViewById(R.id.rv_items)
        rvItems.layoutManager = LinearLayoutManager(this)
        itemsAdapter = ItemsAdapter(
            listData.listItems,
            { position -> deleteItem(position) },
            { updateJsonFile() }
        )

        rvItems.adapter = itemsAdapter

        // Sett overskrift
        val tvListTitle: TextView = findViewById(R.id.tv_list_title)
        tvListTitle.text = "Current List: ${listData.listName}"

        // Legg til nytt element
        val editTextNewItem: EditText = findViewById(R.id.et_new_item)
        val buttonAddItem: Button = findViewById(R.id.btn_add_item)

        buttonAddItem.setOnClickListener {
            val newItemText = editTextNewItem.text.toString().trim()
            if (newItemText.isNotEmpty()) {
                val newItem = ListItem(newItemText, false)
                listData.listItems.add(newItem)
                itemsAdapter.notifyItemInserted(listData.listItems.size - 1)
                editTextNewItem.text.clear()
                storageManager.writeList(fileName, listData) // Oppdater JSON-fil
                logJsonFile("groceries.json")
            }
        }
    }

    private fun deleteItem(position: Int) {
        // Fjern elementet fra listen
        listData.listItems.removeAt(position)
        itemsAdapter.notifyItemRemoved(position)

        // Oppdater JSON-filen
        val fileName = "groceries.json"
        storageManager.writeList(fileName, listData)
    }

    private fun updateJsonFile() {
        val fileName = "groceries.json"
        storageManager.writeList(fileName, listData)
    }


    private fun logJsonFile(fileName: String) {
        val file = File(filesDir, fileName)
        if (file.exists()) {
            val jsonContent = file.readText()
            Log.d("JSONContent", jsonContent)
        } else {
            Log.d("JSONContent", "File does not exist")
        }
    }




}
