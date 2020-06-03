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
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class NoteManagerUnitTest {

    @Rule
    public TemporaryFolder mTempFolder = new TemporaryFolder();
    public NoteManager mNoteManager = NoteManager.getInstance();

    @Mock
    Context mockContext;

    @Before
    public void setUp() throws IOException {
        initMocks(this);
        when(mockContext.getFilesDir()).thenReturn(mTempFolder.newFolder());
    }

    @Before
    public void createData() {
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String tag = "";
        mNoteManager.addNote(title, content, tag);
    }

    public void createImportExportData() {
        int result = mNoteManager.addNote("note1", "first note", "");
        assertEquals(result, 0);
        result = mNoteManager.addNote("note2", "second note", "taggy-tag");
        assertEquals(result, 0);
        result = mNoteManager.addNote("note3", "third note", "");
        assertEquals(result, 0);
        result = mNoteManager.addNote("note4", "fourth note, semicolon detected", "tag");
        assertEquals(result, 0);
    }

    @After
    public void resetNoteList() {
        NoteManager noteManager = NoteManager.getInstance();
        noteManager.getNotes().clear();
        mTempFolder.delete();
    }

    @Test
    public void addNewNoteTest() {
        Integer id = 2;
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
        assertEquals(noteManager.getNoteById(id),null);
        assert(noteManager.getNotes().size() == 1);

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
        mNoteManager.deleteNote(mNoteManager.getNoteById(id));
        assert(mNoteManager.getNotes().size() == 0);

        createData();

        Note note = mNoteManager.getNoteById(3);
        mNoteManager.deleteNote(note);
        assertEquals(mNoteManager.getNotes().size(), 1);
    }

    @Test
    public void updateNoteTest() {
        Integer id = 1;
        String changedContent = "Changed content!";
        String changedTitle = "Test2";

        mNoteManager.getNoteById(id).setContent(changedContent);
        mNoteManager.getNoteById(id).setTitle(changedTitle);
        mNoteManager.getNoteById(id).setLastModification(System.currentTimeMillis());
        assertEquals(mNoteManager.getNoteById(id).getContent(), changedContent);
        assertEquals(mNoteManager.getNoteById(id).getTitle(), changedTitle);
        assertNotEquals(mNoteManager.getNoteById(id).getLastModification().compareTo(mNoteManager.
                getNoteById(id).getCreationTimestamp()), 0);

        changedTitle = "note update";
        changedContent = "me was updated!";
        String changedTag = "updateTag";

        Note note = mNoteManager.getNoteById(id);
        note.setTitle(changedTitle);
        note.setContent(changedContent);
        note.setTag(changedTag);
        mNoteManager.updateNote(note, true);
        assertEquals(mNoteManager.getNoteById(id).getTitle(), changedTitle);
        assertEquals(mNoteManager.getNoteById(id).getContent(), changedContent);
        assertEquals(mNoteManager.getNoteById(id).getTag(), changedTag);
    }

    @Test
    public void getNextIDTest() {
        Integer nextId = mNoteManager.getNextFreeId();
        assert(nextId == 2);

        createData();
        Integer testId = mNoteManager.getNextFreeId();
        assert(testId == 3);

        createData();
        nextId = mNoteManager.getNextFreeId();
        assert(nextId == 4);

        mNoteManager.deleteNote(mNoteManager.getNoteById(2));
        nextId = mNoteManager.getNextFreeId();
        assert(nextId == 2);

        createData();
        nextId = mNoteManager.getNextFreeId();
        assert(nextId == 4);

        createData();
        nextId = mNoteManager.getNextFreeId();
        assert(nextId == 5);

        mNoteManager.deleteNote(mNoteManager.getNoteById(1));
        nextId = mNoteManager.getNextFreeId();
        assert(nextId == 1);
    }

    @Test
    public void addTagTest() {
        Integer id = 2;
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
        mNoteManager.getNoteById(id).setTag("sport");

        // act
        mNoteManager.deleteTagOfNote(id);

        // assert
        assertEquals("", mNoteManager.getNoteById(id).getTag());
    }

    @Test
    public void emptyNotesTest() {
        createData();

        assert(mNoteManager.getNotes().size() == 2);

        mNoteManager.emptyNotes();

        assert(mNoteManager.getNotes().size() == 0);
    }


    /************ Import/Export Test **************/

    @Test
    public void noExportWithNoNotesTest() {
        mNoteManager.emptyNotes();

        int result = mNoteManager.exportNotes(mockContext);
        assertEquals(result, -1);
    }

    @Test
    public void exportDataTest() {
        mNoteManager.emptyNotes();
        createImportExportData();

        // export the notes
        int result = mNoteManager.exportNotes(mockContext);
        assertEquals(result, 0);
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
        assert(fileContent.get(4).contains("fourth note, semicolon detected"));
        assert(fileContent.get(2).contains("tag"));
        assert(fileContent.get(4).contains("False"));
        assert(!fileContent.get(4).contains("taggy-tag"));
    }

    @Test
    public void importDataTest() {
        mNoteManager.emptyNotes();
        createImportExportData();

        List<Note> copiedNotes = mNoteManager.getNotes();
        mNoteManager.exportNotes(mockContext);

        // now delete all notes to import them again
        mNoteManager.emptyNotes();

        assert(mNoteManager.getNotes().size() == 0);

        //import them again
        File[] files = mockContext.getFilesDir().listFiles();
        assertEquals(files.length, 1);
        File file = files[0];

        mNoteManager.importNotes(file);

        assertEquals(mNoteManager.getNotes().size(), copiedNotes.size());
        assertEquals(mNoteManager.getNotes(), copiedNotes);
    }

}