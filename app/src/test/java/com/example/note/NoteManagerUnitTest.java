package com.example.note;

import com.example.note.controller.NoteManager;
import com.example.note.model.Note;

import org.junit.After;
import org.junit.Test;

public class NoteManagerUnitTest {

    @After
    public void resetNoteList() {
        NoteManager noteManager = NoteManager.getInstance();
        noteManager.getNotes().clear();
    }

    @Test
    public void addNewNoteTest() {
        Integer id = 1;
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        NoteManager noteManager = NoteManager.getInstance();
        Note newNote = new Note(id, title, content);
        noteManager.addNote(newNote);
        assert(noteManager.getNodeById(id).getTitle().equals(title));
        assert(noteManager.getNodeById(id).getContent().equals(content));
        assert(noteManager.getNodeById(id).getCreationTimestamp() == noteManager.getNodeById(id).getLastModification());
    }

    @Test
    public void deleteNoteTest() {
        Integer id = 1;
        String title = "Test1";
        NoteManager noteManager = NoteManager.getInstance();
        Note newNote = new Note(id, title);
        noteManager.addNote(newNote);
        noteManager.deleteNote(newNote);
        assert(noteManager.getNotes().size() == 0);
    }

    @Test
    public void updateNoteTest() {
        Integer id = 1;
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String changedContent = "Changed content!";
        String changedTitle = "Test2";
        NoteManager noteManager = NoteManager.getInstance();
        Note newNote = new Note(id, title, content);
        noteManager.addNote(newNote);
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {

        }
        noteManager.getNodeById(id).setContent(changedContent);
        noteManager.getNodeById(id).setTitle(changedTitle);
        noteManager.getNodeById(id).setLastModification(System.currentTimeMillis());
        assert(noteManager.getNodeById(id).getContent().equals(changedContent));
        assert(noteManager.getNodeById(id).getTitle().equals(changedTitle));
        assert(noteManager.getNodeById(id).getLastModification().compareTo(noteManager.getNodeById(id).getCreationTimestamp()) > 0);
    }

    @Test
    public void getNextIDTest() {
        Integer id = 1;
        NoteManager noteManager = NoteManager.getInstance();
        Note newNote = new Note(id);
        noteManager.addNote(newNote);

        Integer nextId = noteManager.getNextFreeId();
        assert(nextId == 2);
        Note testNote = new Note(nextId);
        noteManager.addNote(testNote);

        Integer id_four = 4;
        Note nextNote = new Note(id_four);
        noteManager.addNote(nextNote);

        Integer testId = noteManager.getNextFreeId();
        assert(testId == 3);
    }
}
