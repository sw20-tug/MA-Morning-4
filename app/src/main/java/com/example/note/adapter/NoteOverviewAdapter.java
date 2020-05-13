package com.example.note.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.note.R;
import com.example.note.controller.NoteManager;
import com.example.note.fragments.NoteOverviewFragment;
import com.example.note.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class NoteOverviewAdapter extends ArrayAdapter<Note> implements Observer {
    private final Context context;
    private List<Note> mAllNotes;
    private NoteManager mNoteManager;
    private Fragment currentFragment;

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy", Locale.GERMAN);

    public NoteOverviewAdapter(@NonNull Context context, int resource, List<Note> allNotes, Fragment currentFragment) {
        super(context,resource, allNotes);
        this.context = context;
        this.mAllNotes = allNotes;
        this.mNoteManager = NoteManager.getInstance();
        this.currentFragment = currentFragment;

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.overview_list_item, null);

        LinearLayout header_layout = rowView.findViewById(R.id.header);
        TextView header = rowView.findViewById(R.id.overview_note_header);

        TextView note_title = rowView.findViewById(R.id.overview_note_title);
        TextView note_description = rowView.findViewById(R.id.overview_note_description);
        TextView note_tag = rowView.findViewById(R.id.overview_note_tag);
        TextView note_timestamp = rowView.findViewById(R.id.overview_note_timestamp);

        final Note note = mAllNotes.get(position);

        if (position == 0 && note.isPinned()) {
            header_layout.setVisibility(View.VISIBLE);
        } else if(position > 0 && mAllNotes.get(position-1).isPinned() && !note.isPinned()) {
            header_layout.setVisibility(View.VISIBLE);
            header.setVisibility(View.GONE);
        }

        note_title.setText(note.getTitle());
        note_description.setText(note.getContent());
        note_tag.setText(note.getTag());
        note_timestamp.setText(TIME_FORMATTER.format(note.getLastModification()));

        ImageButton moreButton = rowView.findViewById(R.id.overview_item_more_btn);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Actions");

                String[] actions = {(!note.isPinned()) ? "Pin Note" : "Unpin Note", "Edit Note", "Delete Note", "Delete Tag"};
                builder.setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                note.setPinned(!note.isPinned());
                                mNoteManager.updateNote(note, false);
                                ((NoteOverviewFragment) currentFragment).sortNotesList("Date");
                                notifyDataSetChanged();
                                break;
                            case 1:
                                Bundle bundle = new Bundle();
                                bundle.putInt("note_id", note.getId());
                                NavHostFragment.findNavController(currentFragment)
                                        .navigate(R.id.action_overview_to_detail_fragment, bundle);
                                break;
                            case 2:
                                new AlertDialog.Builder(context)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Delete Note")
                                        .setMessage("Are you sure you want to delete note " +note.getTitle() + " ?")
                                        .setNegativeButton("No", null)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mNoteManager.deleteNote(note);
                                                mAllNotes.remove(note);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                                break;
                            case 3:
                                new AlertDialog.Builder(context)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Delete Tag")
                                        .setMessage("Are you sure you want to delete the Tags " +note.getTag() + " ?")
                                        .setNegativeButton("No", null)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mNoteManager.deleteTagOfNote(note.getId());
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rowView;
    }

    @Override
    public void update(Observable o, Object arg) {
        mAllNotes.clear();
        mAllNotes.addAll((List<Note>) arg);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}