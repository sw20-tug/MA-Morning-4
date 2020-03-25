package com.example.note;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.note.dao.NoteDAO;
import com.example.note.db.AppDatabase;
import com.example.note.db.DatabaseHelper;
import com.example.note.model.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class NoteDAOInstrumentedTest {
    private AppDatabase testDB;
    private NoteDAO noteDAO;

    private String title = "title";
    private String content = "content";
    private String title2 = "title2";
    private String content2 = "content2";
    private String newTitle = "new title";
    private String newContent = "new content";

    @Before
    public void initDB() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseHelper.getInstance().initDb(appContext);
        testDB = DatabaseHelper.getInstance().getDb();
        noteDAO = testDB.noteDAO();
    }

    @After
    public void closeDB() {
        List<Note> notes = noteDAO.getNotes();
        for(Note note : notes) {
            noteDAO.deleteNote(note);
        }
        testDB.close();
    }

    @Test
    public void getNoteById() {
        int id = 1;
        Note testNote = new Note(id,title, content);
        noteDAO.insertNotes(testNote);

        assert(noteDAO.getNoteById(id).equals(testNote));
    }

    @Test
    public void addNote() {
        int id = 2;
        Note testNote = new Note(id,title, content);
        noteDAO.insertNotes(testNote);

        assert(noteDAO.getNotes().get(0).equals(testNote));
    }

    @Test
    public void addMultipleNotes() {
        int offset = 3;
        Note[] testNotes = new Note[10];
        for(int i = offset; i <= 12; i++) {
            Note testNote = new Note(i, title, content);
            testNotes[i - offset] = testNote;
        }
        noteDAO.insertNotes(testNotes);

        for(int i = offset; i <= 12; i++) {
            assert (noteDAO.getNotes().get(i).equals(testNotes[i - offset]));
        }

    }

    @Test
    public void deleteNote() {
        int id1 = 13;
        int id2 = 14;
        Note note1 = new Note(id1, title, content);
        Note note2 = new Note(id2,title2,content2);
        noteDAO.insertNotes(note1, note2);
        noteDAO.deleteNote(note1);

        assert(noteDAO.getNoteById(id1) == null);
        assert(noteDAO.getNoteById(id2).equals(note2));
    }

    @Test
    public void updateNote() {
        int id = 15;
        Note testNote = new Note(id,title, content);
        noteDAO.insertNotes(testNote);
        testNote.setTitle(newTitle);
        testNote.setContent(newContent);
        noteDAO.updateNotes(testNote);

        assert(noteDAO.getNoteById(id).getTitle().equals(newTitle));
        assert(noteDAO.getNoteById(id).getContent().equals(newContent));
        assert(noteDAO.getNoteById(id).getLastModification() > noteDAO.getNoteById(id).getCreationTimestamp());
    }

    @Test
    public void updateMultipleNotes() {
        int offset = 16;
        Note[] testNotes = new Note[10];
        Note[] updateNotes = new Note[10];
        for(int i = offset; i <= 25; i++) {
            Note testNote = new Note(i, title, content);
            testNotes[i - offset] = testNote;
        }
        noteDAO.insertNotes(testNotes);
        for(int i = offset; i <= 25; i++) {
            Note newNote = new Note(i, newTitle, newContent);
            updateNotes[i - offset] = newNote;
        }
        for(int i = offset; i <= 25; i++) {
            assert(noteDAO.getNoteById(i).equals(testNotes[i - offset]));
        }
        noteDAO.updateNotes(updateNotes);
        for(int i = offset; i <= 25; i++) {
            assert(noteDAO.getNoteById(i).equals(updateNotes[i - offset]));
        }

    }

}
