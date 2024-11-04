/**
 * Class: src/edu/ntnu/assignment_07/DatabaseManager
 */
package edu.ntnu.assignment_07

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, "FilmDatabase", null, 1) {
    companion object {
        private const val TABLE_FILM = "Film"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DIRECTOR = "director"
        private const val COLUMN_ACTORS = "actors"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
          CREATE TABLE $TABLE_FILM (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITLE TEXT NOT NULL,
            $COLUMN_DIRECTOR TEXT NOT NULL,
            $COLUMN_ACTORS TEXT NOT NULL
          );
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FILM")
        onCreate(db)
    }

    fun insertFilms(films: List<Film>) {
        writableDatabase.use { db ->
            db.beginTransaction()
            try {
                films.forEach { film ->
                    val values = ContentValues().apply {
                        put(COLUMN_TITLE, film.title)
                        put(COLUMN_DIRECTOR, film.director)
                        put(COLUMN_ACTORS, film.actors.joinToString(", "))
                    }
                    db.insert(TABLE_FILM, null, values)
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
    }

    // Funksjon for å eksportere filmer fra databasen til en fil
    fun exportFilmsToFile(context: Context, filename: String) {
        val films = fetchAllFilms()
        val file = File(context.filesDir, filename)

        file.printWriter().use { out ->
            films.forEach { film ->
                out.println("${film.title}, ${film.director}, ${film.actors.joinToString(", ")}")
            }
        }
    }

    // Public metode for å hente alle filmer fra databasen
    fun fetchAllFilms(): List<Film> {
        val films = mutableListOf<Film>()
        readableDatabase.query(TABLE_FILM, null, null, null, null, null, null).use { cursor ->
            while (cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val director = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR))
                val actors = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTORS)).split(", ")
                films.add(Film(title, director, actors))
                Log.d("DatabaseManager", "Film: $title, $director, $actors")
            }
        }
        return films
    }

    // Utfører en query og returnerer resultatene som en liste av strenger
    fun performQuery(table: String, columns: Array<String>, selection: String? = null): ArrayList<String> {
        val results = ArrayList<String>()
        readableDatabase.query(table, columns, selection, null, null, null, null).use { cursor ->
            while (cursor.moveToNext()) {
                results.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
            }
        }
        return results
    }

    // Utfører en rå query og returnerer resultatene som en liste av strenger
    fun performRawQuery(select: Array<String>, from: Array<String>, join: Array<String>, where: String? = null): ArrayList<String> {
        val query = StringBuilder("SELECT ${select.joinToString(", ")} FROM ${from.joinToString(", ")}")
        if (join.isNotEmpty()) {
            query.append(" ON ${join.joinToString(" AND ")}")
        }
        if (where != null) {
            query.append(" WHERE $where")
        }

        val results = ArrayList<String>()
        readableDatabase.rawQuery(query.toString(), null).use { cursor ->
            while (cursor.moveToNext()) {
                results.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
            }
        }
        return results
    }

    // Henter filmer av en bestemt regissør
    fun getFilmsByDirector(director: String): ArrayList<String> {
        return performQuery(TABLE_FILM, arrayOf(COLUMN_TITLE), "$COLUMN_DIRECTOR = '$director'")
    }

    // Henter filmer med en bestemt skuespiller
    fun getFilmsByActor(actor: String): ArrayList<String> {
        return performRawQuery(
            select = arrayOf(COLUMN_TITLE),
            from = arrayOf(TABLE_FILM),
            join = emptyArray(),
            where = "$COLUMN_ACTORS LIKE '%$actor%'"
        )
    }
}

