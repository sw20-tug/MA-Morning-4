package com.example.note.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.note.R;
import com.example.note.adapter.DoneNotesOverviewAdapter;
import com.example.note.controller.NoteManager;
import com.example.note.dialogs.FilterDialogDone;
import com.example.note.model.Note;
import com.example.note.model.NoteComparator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoneNotesOverviewFragment extends Fragment {

    private DoneNotesOverviewAdapter mAdapter;
    private List<Note> mCompletedNotes;
    private List<Note> mNotesCopy;
    private NoteManager mNoteManager;
    private Button mRemoveFiltersBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.done_notes_overview_fragment, container, false);
        Context context = inflater.getContext();

        mNoteManager = NoteManager.getInstance();
        mCompletedNotes = getCompletedNotes(mNoteManager.getNotes());
        mNotesCopy = new ArrayList<>(mCompletedNotes);

        ListView listView = view.findViewById(R.id.overview_done_notes_list);
        mAdapter = new DoneNotesOverviewAdapter(context, R.layout.overview_list_item, mCompletedNotes, DoneNotesOverviewFragment.this);
        mNoteManager.addObserver(mAdapter);
        listView.setAdapter(mAdapter);

        mRemoveFiltersBtn = view.findViewById(R.id.removeFilterBtn);
        mRemoveFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCompletedNotes.clear();
                mCompletedNotes.addAll(mNotesCopy);
                mRemoveFiltersBtn.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton back_all_notes_btn = view.findViewById(R.id.overview_back_all_notes_btn);
        back_all_notes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(DoneNotesOverviewFragment.this)
                        .navigate(R.id.action_done_overview_to_overview_fragment);
            }
        });

        sortNotesList("Date");
        mAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if(visible && mAdapter != null) {
            mCompletedNotes = getCompletedNotes(mNoteManager.getNotes());
            mAdapter.notifyDataSetChanged();
        }
    }

    public List<Note> getAllNotes() {
        return mCompletedNotes;
    }

    private List<Note> getCompletedNotes(List<Note> allNotes) {
        List<Note> completedNotes = new ArrayList<>();

        for(Note note : allNotes) {
            if(note.isMarkedAsDone())
                completedNotes.add(note);
        }

        return completedNotes;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort) {
            showSortDialog();
            return true;
        }
        else if (id == R.id.filter) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sortNotesList(String categorie) {
        if(categorie.equals("Title") || categorie.equals("Date"))
            Collections.sort(mCompletedNotes, new NoteComparator(categorie));
        else if(categorie.equals("Title desc") || categorie.equals("Date desc"))
            Collections.sort(mCompletedNotes, new NoteComparator(categorie).reversed());
        mAdapter.notifyDataSetChanged();
    }

    private void showSortDialog() {
        final CharSequence[] items = {"Title", "Title desc", "Date", "Date desc"};
        new AlertDialog.Builder(DoneNotesOverviewFragment.super.getContext())
                .setTitle("Sort Notes")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        int i = lw.getCheckedItemPosition();
                        sortNotesList(items[i].toString());                    }
                })
                .show();
    }

    private void showFilterDialog() {
        FilterDialogDone dialog = new FilterDialogDone(this);
        dialog.setTitle(R.string.filter);
        dialog.show();
    }

    public void filterByTagAndDate(String tag, Long from, Long to) {
        mNotesCopy = new ArrayList<>(mCompletedNotes);
        final Long fromVal = (from == null ? Long.MIN_VALUE : from);
        final Long toVal = (to == null ? Long.MAX_VALUE : to);
        mCompletedNotes.removeIf(note -> (!note.getTag().equals(tag) || note.getLastModification() > toVal || note.getLastModification() < fromVal));
        mRemoveFiltersBtn.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

}
