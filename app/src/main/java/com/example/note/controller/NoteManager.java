package com.example.note.controller;

import com.example.note.db.DeleteNotesTask;
import com.example.note.db.InsertNotesTask;
import com.example.note.db.UpdateNotesTask;
import com.example.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    private static NoteManager instance = null;

    private NoteManager() {
        notes = new ArrayList<>();
    };

    private List<Note> notes;

    public static NoteManager getInstance() {
        if(instance == null) {
            instance = new NoteManager();
        }
        return instance;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(String title, String description, String tag) {
        final Note note = new Note(getNextFreeId(), title, description, tag);
        notes.add(note);
        new InsertNotesTask().execute(note);
    }

    public Note getNoteById(int id) {
      for(Note note : notes) {
          if(note.getId() == id)
            return note;
      }
      return null;
    }

    public Integer getNextFreeId() {
        int id = 1;
        while(true) {
            if(getNoteById(id) == null)
                return id;
            id++;
        }
    }

    public void updateNote(Note note, boolean set_modification) {
        if(set_modification)
            note.setLastModification(System.currentTimeMillis());
        new UpdateNotesTask().execute(note);
    }

    public void deleteNote(Note note) {
        notes.remove(note);
        new DeleteNotesTask().execute(note);
    }

    public void importNotes() {}

    public void exportNotes() {}

    public void shareNote(int noteId) {}

}
