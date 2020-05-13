package com.example.note;

import com.example.note.controller.NoteManager;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoteManagerUnitTest {

    @After
    public void resetNoteList() {
        NoteManager noteManager = NoteManager.getInstance();
        noteManager.getNotes().clear();
    }

    @Test
    public void addNewNoteTest() {
        Integer id = 1;
        String title = "";
        String content = "Hallo, ich bin ein Testnote!";
        String tag = "";

        NoteManager noteManager = NoteManager.getInstance();
        int result;
        result = noteManager.addNote(title, content, tag);
        assertEquals(result, -1);
        title = "ab";
        result = noteManager.addNote(title, content, tag);
        assertEquals(result, -1);
        assert(noteManager.getNoteById(id) == null);
        assert(noteManager.getNotes().size() == 0);

        title = "Test1";
        result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);
        assert(noteManager.getNoteById(id).getTitle().equals(title));
        assert(noteManager.getNoteById(id).getContent().equals(content));
        assert(noteManager.getNoteById(id).getCreationTimestamp() == noteManager.getNoteById(id)
                .getLastModification());
    }

    @Test
    public void deleteNoteTest() {
        Integer id = 1;
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String tag = "";

        NoteManager noteManager = NoteManager.getInstance();
        int result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);
        noteManager.deleteNote(noteManager.getNoteById(id));
        assert(noteManager.getNotes().size() == 0);
    }

    @Test
    public void updateNoteTest() {
        Integer id = 1;
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String changedContent = "Changed content!";
        String changedTitle = "Test2";
        String tag = "";

        NoteManager noteManager = NoteManager.getInstance();
        int result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {

        }
        noteManager.getNoteById(id).setContent(changedContent);
        noteManager.getNoteById(id).setTitle(changedTitle);
        noteManager.getNoteById(id).setLastModification(System.currentTimeMillis());
        assert(noteManager.getNoteById(id).getContent().equals(changedContent));
        assert(noteManager.getNoteById(id).getTitle().equals(changedTitle));
        assert(noteManager.getNoteById(id).getLastModification().compareTo(noteManager.
                getNoteById(id).getCreationTimestamp()) > 0);
    }

    @Test
    public void getNextIDTest() {
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String tag = "";

        NoteManager noteManager = NoteManager.getInstance();
        int result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);

        Integer nextId = noteManager.getNextFreeId();
        assert(nextId == 2);

        result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);
        Integer testId = noteManager.getNextFreeId();
        assert(testId == 3);
    }

    @Test
    public void addTagTest() {
        Integer id = 1;
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String tag = "sport";

        NoteManager noteManager = NoteManager.getInstance();
        int result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);

        assertEquals(tag, noteManager.getNoteById(id).getTag());
    }

    @Test
    public void removeTagTest() {
        // arrange
        Integer id = 1;
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String tag = "sport";

        NoteManager noteManager = NoteManager.getInstance();
        int result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);

        // act
        noteManager.deleteTagOfNote(id);
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}

        // assert
        assertEquals("", noteManager.getNoteById(id).getTag());
    }

}
