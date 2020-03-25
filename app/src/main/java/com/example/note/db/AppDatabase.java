package com.example.note.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.note.dao.NoteDAO;
import com.example.note.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDAO noteDAO();
}