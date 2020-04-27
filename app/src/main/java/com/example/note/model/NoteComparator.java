package com.example.note.model;

import java.util.Comparator;

public class NoteComparator implements Comparator<Note> {

    private String mCategorie;
    private boolean isReversed;
    public NoteComparator(String categorie) {
        mCategorie = categorie;
        isReversed = categorie.contains("desc");
    }
    @Override
    public int compare(Note firstNote, Note secondNote) {
        int result = 0;

        if (!firstNote.isPinned() && secondNote.isPinned())
            return isReversed ? -1 : 1;
        if(firstNote.isPinned() && !secondNote.isPinned())
            return isReversed ? 1 : -1;
        if(mCategorie.contains("Date"))
            result = firstNote.getLastModification().compareTo(secondNote.getLastModification());
        else
            result = firstNote.getTitle().compareToIgnoreCase(secondNote.getTitle());
        return result;
    }
}
