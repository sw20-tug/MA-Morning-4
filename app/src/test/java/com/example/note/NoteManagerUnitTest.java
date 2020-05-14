package com.example.note;

import com.example.note.controller.NoteManager;
import com.example.note.model.Note;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

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

        result = noteManager.addNote(title, content, tag);
        assertEquals(noteManager.getNotes().size(), 1);

        Note note = noteManager.getNoteById(3);
        noteManager.deleteNote(note);
        assertEquals(noteManager.getNotes().size(), 1);
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

        title = "note update";
        content = "me was updated!";
        tag = "updateTag";

        Note note = noteManager.getNoteById(id);
        note.setTitle(title);
        note.setContent(content);
        note.setTag(tag);
        noteManager.updateNote(note, true);
        assert(noteManager.getNoteById(id).getTitle() == title);
        assert(noteManager.getNoteById(id).getContent() == content);
        assert(noteManager.getNoteById(id).getTag() == tag);
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
        result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);

        nextId = noteManager.getNextFreeId();
        assert(nextId == 4);

        noteManager.deleteNote(noteManager.getNoteById(2));
        nextId = noteManager.getNextFreeId();
        assert(nextId == 2);

        result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);
        nextId = noteManager.getNextFreeId();
        assert(nextId == 4);
        result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);
        nextId = noteManager.getNextFreeId();
        assert(nextId == 5);

        noteManager.deleteNote(noteManager.getNoteById(1));
        nextId = noteManager.getNextFreeId();
        assert(nextId == 1);
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

    @Test
    public void emptyNotesTest() {
        String title = "Note1";
        String content = "my first note";
        String tag = "";

        NoteManager noteManager = NoteManager.getInstance();
        int result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);

        title = "Note2";
        content = "my second note";
        tag = "taggy-tag";

        result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);

        assert(noteManager.getNotes().size() == 2);

        noteManager.emptyNotes();

        assert(noteManager.getNotes().size() == 0);
    }

}
