/**
 * src/main/java/edu/ntnu/assignment_07/DatabaseManager.kt
 */

package edu.ntnu.assignment_07

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FilmDatabase"
        private const val DATABASE_VERSION = 1

        private const val TABLE_FILM = "FILM"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DIRECTOR = "director"
        private const val COLUMN_ACTORS = "actors"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_FILM (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DIRECTOR TEXT NOT NULL,
                $COLUMN_ACTORS TEXT NOT NULL
            );
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FILM")
        onCreate(db)
    }

    fun insertFilm(film: Film): Long {
        if (!filmExists(film.title, film.director)) {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, film.title)
                put(COLUMN_DIRECTOR, film.director)
                put(COLUMN_ACTORS, film.actors.joinToString(", "))
            }

            return db.insert(TABLE_FILM, null, values).also {
                db.close()
            }
        }
        return -1  // Indikerer at filmen allerede finnes
    }

    private fun filmExists(title: String, director: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FILM,
            arrayOf(COLUMN_ID),
            "$COLUMN_TITLE = ? AND $COLUMN_DIRECTOR = ?",
            arrayOf(title, director),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun insertFilms(films: List<Film>) {
        films.forEach { insertFilm(it) }
    }

    fun getAllFilms(): List<Film> {
        val films = mutableListOf<Film>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_FILM, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val director = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR))
                val actors = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTORS)).split(", ").map { it.trim() }
                films.add(Film(title, director, actors))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return films
    }

    fun clearDatabase() {
        writableDatabase.use { db ->
            db.execSQL("DELETE FROM $TABLE_FILM")
        }
    }

    fun getFilmsByDirector(director: String): List<Film> {
        val films = mutableListOf<Film>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FILM,
            null,
            "$COLUMN_DIRECTOR = ?",
            arrayOf(director),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val directorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR))
                val actors = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTORS)).split(", ").map { it.trim() }
                films.add(Film(title, directorName, actors))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return films
    }

    fun getFilmsByActor(actor: String): List<Film> {
        val films = mutableListOf<Film>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FILM,
            null,
            "$COLUMN_ACTORS LIKE ?",
            arrayOf("%$actor%"),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val directorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR))
                val actors = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTORS)).split(", ").map { it.trim() }
                films.add(Film(title, directorName, actors))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return films
    }

    fun getUniqueDirectors(): List<String> {
        val directors = mutableSetOf<String>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FILM,
            arrayOf(COLUMN_DIRECTOR),
            null,
            null,
            COLUMN_DIRECTOR,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val director = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR))
                directors.add(director)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return directors.toList().sorted() // Sorterer alfabetisk
    }

    fun getUniqueActors(): List<String> {
        val actors = mutableSetOf<String>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FILM,
            arrayOf(COLUMN_ACTORS),
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val actorList = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTORS)).split(", ")
                actors.addAll(actorList)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return actors.toList().sorted() // Sorterer alfabetisk
    }

}

// End of class DatabaseManager
