package com.example.note.controller;

import com.example.note.db.DeleteNotesTask;
import com.example.note.db.EmptyNotesTableTask;
import com.example.note.db.InsertNotesTask;
import com.example.note.db.UpdateNotesTask;
import com.example.note.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class NoteManager extends Observable {
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

    public int addNote(String title, String description, String tag) {
        if(title == null || title.length() < 3){
            return -1;
        }
        final Note note = new Note(getNextFreeId(), title, description, tag);
        notes.add(note);
        new InsertNotesTask().execute(note);
        return 0;
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

    public void emptyNotes() {
        notes.clear();
        notifyObservers(notes);
        new EmptyNotesTableTask().execute();
    }

    public void importNotes() {}

    public void exportNotes() {}

    public void shareNote(int noteId) {}

    public void deleteTagOfNote(int i) {
        Note note = getNoteById(i);
        note.setTag("");
        updateNote(note, true);
    }
}
