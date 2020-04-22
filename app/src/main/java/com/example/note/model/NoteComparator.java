package com.example.note.model;

import java.util.Comparator;

public class NoteComparator implements Comparator<Note> {

    private String mCategorie;

    public NoteComparator(String categorie) {
        mCategorie = categorie;
    }
    @Override
    public int compare(Note firstNote, Note secondNote) {
        if(mCategorie.equals("Date"))
            return firstNote.getLastModification().compareTo(secondNote.getLastModification());
        else
            return firstNote.getTitle().compareToIgnoreCase(secondNote.getTitle());
    }
}
