package com.example.notes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COL_ID="id"
        private const val COL_TITLE="title"
        private const val COL_CONTENT="content"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY,$COL_TITLE TEXT,$COL_CONTENT TEXT)"
         db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE,note.title)
            put(COL_CONTENT,note.content)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }
    fun getAllNotes(): List<Note>{
        val noteslist = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT))

            val note = Note(id, title, content)
            noteslist.add(note)

        }

        cursor.close()
        db.close()
        return noteslist
    }
    fun updateNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COL_TITLE,note.title)
            put(COL_CONTENT,note.content)
        }
        val whereClause = "$COL_ID=?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }

    fun getNoteByID(noteID: Int): Note{
        val db=readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID=$noteID"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT))

        cursor.close()
        db.close()
        return Note(id,title,content)


    }
    fun deleteNote(noteID: Int){
        val db = writableDatabase
        val whereClause = "$COL_ID = ?"
        val whereArgs = arrayOf(noteID.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }
}