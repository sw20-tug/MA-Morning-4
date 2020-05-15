package com.example.note;

import android.content.Context;

import com.example.note.controller.NoteManager;
import com.example.note.model.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class NoteManagerUnitTest {

    @Rule
    public TemporaryFolder mTempFolder = new TemporaryFolder();

    @Mock
    Context mockContext;

    @Before
    public void setUp() throws IOException {
        initMocks(this);
        when(mockContext.getFilesDir()).thenReturn(mTempFolder.newFolder());
    }

    @After
    public void resetNoteList() {
        NoteManager noteManager = NoteManager.getInstance();
        noteManager.getNotes().clear();
        mTempFolder.delete();
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


    /************ Import/Export Test **************/

    @Test
    public void exportDataTest() {
        NoteManager noteManager = NoteManager.getInstance();
        int result = 0;

        // Create data
        result = noteManager.addNote("note1", "first note", "");
        assert(result == 0);
        result = noteManager.addNote("note2", "second note", "taggy-tag");
        assert(result == 0);
        result = noteManager.addNote("note3", "third note", "");
        assert(result == 0);
        result = noteManager.addNote("note4", "fourth note", "tag");
        assert(result == 0);

        // export the notes
        noteManager.exportNotes(mockContext);
        File[] files = mockContext.getFilesDir().listFiles();
        assert (files.length == 1);
        File file = files[0];
        List<String> fileContent = new ArrayList<String>();
        try {
            fileContent = Files.readAllLines(Paths.get(String.valueOf(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert(fileContent.size() == 5); // 4 notes plus header

        //first note
        assert(fileContent.get(1).contains("note1"));
        assert(fileContent.get(1).contains("first note"));
        assert(fileContent.get(1).contains("False"));
        assert(!fileContent.get(1).contains("tag"));
        assert(!fileContent.get(1).contains("taggy-tag"));

        //second note
        assert(fileContent.get(2).contains("note2"));
        assert(fileContent.get(2).contains("second note"));
        assert(fileContent.get(2).contains("taggy-tag"));
        assert(fileContent.get(2).contains("False"));

        //third note
        assert(fileContent.get(3).contains("note3"));
        assert(fileContent.get(3).contains("third note"));
        assert(fileContent.get(3).contains("False"));
        assert(!fileContent.get(3).contains("tag"));
        assert(!fileContent.get(3).contains("taggy-tag"));

        //fourth note
        assert(fileContent.get(4).contains("note4"));
        assert(fileContent.get(4).contains("fourth note"));
        assert(fileContent.get(2).contains("tag"));
        assert(fileContent.get(4).contains("False"));
        assert(!fileContent.get(4).contains("taggy-tag"));
    }

    @Test
    public void importDataTest() {
        assert(true);
    }

}