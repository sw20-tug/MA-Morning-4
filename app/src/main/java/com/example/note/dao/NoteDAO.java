package com.example.note.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.note.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM note")
    public List<Note> getNotes();

    @Query("SELECT * FROM note WHERE id IN (:noteId) LIMIT 1")
    public Note getNoteById(int noteId);

    @Insert
    public void insertNotes(Note... note);

    @Delete
    public void deleteNote(Note note);

    @Update
    public void updateNotes(Note... note);

    @Query("DELETE FROM note")
    public void clearTable();
}
