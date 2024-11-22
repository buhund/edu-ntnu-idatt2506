package edu.ntnu.assignment_08_project.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ntnu.assignment_08_project.R
import edu.ntnu.assignment_08_project.data.StorageManager

class MainScreenActivity : AppCompatActivity() {

  private lateinit var storageManager: StorageManager
  private lateinit var listsAdapter: ListsAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main_screen)

    // Initialiser StorageManager
    storageManager = StorageManager(this)

    // Hent tilgjengelige lister
    val listFiles = storageManager.getAllLists()

    // RecyclerView-oppsett
    val rvLists: RecyclerView = findViewById(R.id.rv_lists)
    rvLists.layoutManager = LinearLayoutManager(this)
    listsAdapter = ListsAdapter(listFiles) { fileName ->
      openList(fileName) // Åpne valgt liste
    }
    rvLists.adapter = listsAdapter

    // Opprett ny liste
    val btnNewList: Button = findViewById(R.id.btn_new_list)
    btnNewList.setOnClickListener {
      createNewList()
    }
  }

  private fun openList(fileName: String) {
    val intent = Intent(this, ListActivity::class.java)
    intent.putExtra("fileName", fileName)
    startActivity(intent)
  }

  private fun createNewList() {
    val newListName = "New List ${System.currentTimeMillis()}"
    val fileName = "$newListName.json"
    storageManager.createList(fileName, newListName)
    listsAdapter.addList(fileName)
    Log.d("MainScreenActivity", "Created new list: $fileName")
  }
}