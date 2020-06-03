package com.example.note;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.note.controller.NoteManager;
import com.example.note.dao.NoteDAO;
import com.example.note.db.AppDatabase;
import com.example.note.db.DatabaseHelper;
import com.example.note.model.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NoteSendMailTest {

    private AppDatabase testDB;
    private NoteDAO noteDAO;

    @Rule
    public IntentsTestRule<MainActivity> mActivity = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void initDB() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseHelper.getInstance().initDb(appContext);
        testDB = DatabaseHelper.getInstance().getDb();
        noteDAO = testDB.noteDAO();
        noteDAO.insertNotes();
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
    public void sendEmailTest() {
        String title = "Test1";
        String content = "Hallo, ich bin ein Testnote!";
        String tag = "sport";

        NoteManager noteManager = NoteManager.getInstance();
        int result = noteManager.addNote(title, content, tag);
        assertEquals(result, 0);

        onView(withId(R.id.show_done_notes))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.overview_back_all_notes_btn))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.overview_list))
                .check(matches(isDisplayed()));

        onData(anything())
                .inAdapterView(withId(R.id.overview_list))
                .atPosition(0)
                .onChildView(withId(R.id.overview_item_more_btn))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(R.string.send_via_email))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        intended(allOf(hasAction(Intent.ACTION_CHOOSER),
                hasExtra(Intent.EXTRA_TITLE, "Choose client: "),
                hasExtra(is(Intent.EXTRA_INTENT),
                        hasAction(Intent.ACTION_SEND))));
    }

}
