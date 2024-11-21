package edu.ntnu.assignment_08_project.data

import android.content.Context
import com.google.gson.Gson
import edu.ntnu.assignment_08_project.data.models.ListData
import java.io.File

class StorageManager(private val context: Context) {
    private val gson = Gson()

    // Create new list
    fun createList(fileName: String, listName: String) {
        val listData = ListData(listName, mutableListOf())
        writeList(fileName, listData)
    }

    // Read existing list
    fun readList(fileName: String): ListData? {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            val json = file.readText()
            gson.fromJson(json, ListData::class.java)
        } else {
            null
        }
    }


    // Write updated list to file
    fun writeList(fileName: String, listData: ListData) {
        val file = File(context.filesDir, fileName)
        file.writeText(gson.toJson(listData))
    }

    fun deleteList(fileName: String) {
        val file = File(context.filesDir, fileName)
        if (file.exists()) file.delete()
    }

    // Get all files, i.e. lists
    fun getAllLists(): List<String> {
        return context.filesDir.list()?.toList() ?: emptyList()
    }

}