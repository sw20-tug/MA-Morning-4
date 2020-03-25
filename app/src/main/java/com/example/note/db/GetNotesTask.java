package com.example.note.db;

import android.os.AsyncTask;

import com.example.note.controller.NoteManager;
import com.example.note.dao.NoteDAO;
import com.example.note.model.Note;

import java.util.List;

public class GetNotesTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase db = DatabaseHelper.getInstance().getDb();
        NoteDAO noteDAO = db.noteDAO();
        List<Note> notes = noteDAO.getNotes();
        NoteManager.getInstance().setNotes(notes);
        return null;
    }
}
