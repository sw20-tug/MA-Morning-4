package com.example.note.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.note.controller.NoteManager;

public class DatabaseHelper {
    private static DatabaseHelper instance = null;
    private AppDatabase db = null;

    private DatabaseHelper() {}

    public static DatabaseHelper getInstance() {
        if(instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public void initDb(Context context) {
        db = Room.databaseBuilder(context,
                AppDatabase.class, "note-db").build();

    }

    public AppDatabase getDb() {
        return db;
    }
}
