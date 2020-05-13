package com.example.note;

import com.example.note.controller.NoteManager;
import com.example.note.model.Note;
import com.example.note.model.NoteComparator;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoteComparatorUnitTest {

    String title_asc = "Title";
    String title_des = "Title desc";
    String data_asc = "Date";
    String data_des = "Date desc";
    @After
    public void resetNoteList() {
    }

    @Test
    public void when_orderTitleAscAndSorted_then_returnMinus1() {
        Note first = new Note(1, "a");
        Note second = new Note(2, "b");
        NoteComparator noteComparator  = new NoteComparator(title_asc);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }
    @Test
    public void when_orderTitleAscUnsorted_then_return1() {
        Note first = new Note(2, "b");
        Note second = new Note(1, "a");
        NoteComparator noteComparator  = new NoteComparator(title_asc);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    @Test
    public void when_orderTitleAscAndSortedWithBothPinned_then_returnMinus1() {
        Note first = new Note(1, "a");
        first.setPinned(true);
        Note second = new Note(2, "b");
        second.setPinned(true);
        NoteComparator noteComparator  = new NoteComparator(title_asc);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }

    @Test
    public void when_orderTitleAscAndSortedWithFirstPinned_then_returnMinus1() {
        Note first = new Note(1, "a");
        first.setPinned(true);
        Note second = new Note(2, "b");
        NoteComparator noteComparator  = new NoteComparator(title_asc);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }

    @Test
    public void when_orderTitleAscUnsortedWithSecondePinned_then_return1() {
        Note first = new Note(2, "b");
        Note second = new Note(1, "a");
        second.setPinned(true);
        NoteComparator noteComparator  = new NoteComparator(title_asc);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    // descending
    @Test
    public void when_orderTitleDescAndSortedWithBothPinned_then_return1() {
        Note first = new Note(2, "b");
        first.setPinned(true);
        Note second = new Note(1, "a");
        second.setPinned(true);
        NoteComparator noteComparator  = new NoteComparator(title_des);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    @Test
    public void when_orderTitleDescAndSortedWithFirstPinned_then_return1() {
        Note first = new Note(2, "b");
        first.setPinned(true);
        Note second = new Note(1, "a");
        NoteComparator noteComparator  = new NoteComparator(title_des);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    @Test
    public void when_orderTitleDescUnsortedWithSecondPinned_then_returnMinus1() {
        Note first = new Note(1, "a");
        Note second = new Note(2, "b");
        second.setPinned(true);
        NoteComparator noteComparator  = new NoteComparator(title_des);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }


    // date

    @Test
    public void when_orderDateAscAndSorted_then_returnMinus1() {
        Note first = new Note(1, "a");
        Note second = new Note(2, "b");
        second.setLastModification(second.getLastModification()+100);
        NoteComparator noteComparator  = new NoteComparator(data_asc);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }
    @Test
    public void when_orderDateAscUnsorted_then_return1() {
        Note second = new Note(1, "a");
        Note first = new Note(2, "b");
        first.setLastModification(first.getLastModification()+100);
        NoteComparator noteComparator  = new NoteComparator(data_asc);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    @Test
    public void when_orderDateAscAndSortedWithBothPinned_then_returnMinus1() {
        Note first = new Note(1, "a");
        first.setPinned(true);
        Note second = new Note(2, "b");
        second.setPinned(true);
        second.setLastModification(second.getLastModification()+100);
        NoteComparator noteComparator  = new NoteComparator(data_asc);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }

    @Test
    public void when_orderDateAscAndSortedWithFirstPinned_then_returnMinus1() {
        Note first = new Note(1, "a");
        first.setPinned(true);
        Note second = new Note(2, "b");
        second.setLastModification(second.getLastModification()+100);
        NoteComparator noteComparator  = new NoteComparator(data_asc);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }

    @Test
    public void when_orderDateAscUnsortedWithSecondPinned_then_return1() {
        Note second = new Note(1, "a");
        Note first = new Note(2, "b");
        second.setPinned(true);
        second.setLastModification(second.getLastModification()+100);
        NoteComparator noteComparator  = new NoteComparator(data_asc);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    // descending
    @Test
    public void when_orderDateDescAndSortedWithBothPinned_then_return1() {
        Note first = new Note(2, "b");
        first.setPinned(true);
        Note second = new Note(1, "a");
        second.setPinned(true);
        NoteComparator noteComparator  = new NoteComparator(title_des);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    @Test
    public void when_orderDateDescAndSortedWithFirstPinned_then_return1() {
        Note first = new Note(2, "b");
        first.setPinned(true);
        Note second = new Note(1, "a");
        NoteComparator noteComparator  = new NoteComparator(title_des);

        int value = noteComparator.compare(first, second);
        assert(value == 1);
    }

    @Test
    public void when_orderDateDescUnsortedWithSecondPinned_then_returnMinus1() {
        Note first = new Note(1, "a");
        Note second = new Note(2, "b");
        second.setPinned(true);
        NoteComparator noteComparator  = new NoteComparator(title_des);

        int value = noteComparator.compare(first, second);
        assert(value == -1);
    }
}
