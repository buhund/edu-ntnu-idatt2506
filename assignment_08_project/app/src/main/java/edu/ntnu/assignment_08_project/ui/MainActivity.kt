package edu.ntnu.assignment_08_project.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import edu.ntnu.assignment_08_project.R
import edu.ntnu.assignment_08_project.data.StorageManager
import edu.ntnu.assignment_08_project.data.models.ListData
import edu.ntnu.assignment_08_project.data.models.ListItem

class MainActivity : AppCompatActivity() {
    private lateinit var storageManager: StorageManager
    private lateinit var listData: ListData
    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize storage manager
        storageManager = StorageManager(this)

        // Test: Create, read and write to a new list
        var fileName = "groceries.json"
        storageManager.createList(fileName, "Groceries")

        val listData = storageManager.readList(fileName)
        Log.d("MainActivity", "Read List: $listData")

        listData?.listItems?.add(ListItem("Milk", false))
        storageManager.writeList(fileName, listData!!)

        val updatedListData = storageManager.readList(fileName)
        Log.d("MainActivity", "Updated List: $updatedListData")

        storageManager.deleteList(fileName)
        Log.d("MainActivity", "List deleted")
    }


} // End of class MainActivity