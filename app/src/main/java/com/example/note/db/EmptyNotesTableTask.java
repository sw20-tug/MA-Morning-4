package com.example.note.db;

import android.os.AsyncTask;

import com.example.note.dao.NoteDAO;
import com.example.note.model.Note;

public class EmptyNotesTableTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase db = DatabaseHelper.getInstance().getDb();
        NoteDAO noteDAO = db.noteDAO();
        noteDAO.clearTable();
        return null;
    }
}
