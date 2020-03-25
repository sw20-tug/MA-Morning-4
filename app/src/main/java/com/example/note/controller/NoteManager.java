package com.example.note.controller;

import com.example.note.db.DeleteNotesTask;
import com.example.note.db.GetNotesTask;
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

    public void addNote(String title, String description) {
        final Note note = new Note(getNextFreeId(), title, description);
        notes.add(note);
        new InsertNotesTask().execute(note);
    }

    public Note getNodeById(int id) {
      for(Note note : notes) {
          if(note.getId() == id)
            return note;
      }
      return null;
    }

    public Integer getNextFreeId() {
        int id = 1;
        while(true) {
            if(getNodeById(id) == null)
                return id;
            id++;
        }
    }

    public void updateNote(Note note) {
        new UpdateNotesTask().execute(note);
    }

    public void deleteNote(Note note) {
        notes.remove(note);
        new DeleteNotesTask().execute(note);
    }

    public void importNotes() {}

    public void exportNotes() {}

    // TODO: Future Sprints
    public void addTag(int tagId) {}

    public void editTag(int tagId) {}

    public void removeTag(int tagId) {}

    public void shareNote(int noteId) {}

}
