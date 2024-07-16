package com.example.notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notes.databinding.ActivityUpdateNoteBinding


class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        noteID = intent.getIntExtra("note_id",-1)
        if (noteID == -1){
            finish()
            return
        }

        val note = db.getNoteByID(noteID)
        binding.etupdateTitle.setText(note.title)
        binding.etupdateDescription.setText(note.content)

        binding.updatesave.setOnClickListener{
            val newTitle = binding.etupdateTitle.text.toString()
            val newcontent = binding.etupdateDescription.text.toString()
            val updateNote = Note(noteID,newTitle,newcontent)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this,"Changes Saved",Toast.LENGTH_SHORT).show()
        }




          }
    }
