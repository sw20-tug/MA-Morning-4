package com.example.note.controller;

import com.example.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    private static NoteManager instance = null;
    private NoteManager() {
        this.notes = new ArrayList<>();
    };

    private List<Note> notes;

    public static NoteManager getInstance() {
        if(instance == null) {
            instance = new NoteManager();
        }
        return instance;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public void deleteNote(Note note) {
        notes.remove(note);
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

    public void importNotes() {}

    public void exportNotes() {}

    // TODO: Future Sprints
    public void addTag(int tagId) {}

    public void editTag(int tagId) {}

    public void removeTag(int tagId) {}

    public void shareNote(int noteId) {}

}
