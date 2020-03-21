package com.example.note.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.note.R;
import com.example.note.model.Note;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteOverviewAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private List<Note> mAllNotes;

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy", Locale.GERMAN);

    public NoteOverviewAdapter(@NonNull Context context, int resource, List<Note> allNotes) {
        super(context,resource, allNotes);
        this.context = context;
        this.mAllNotes = allNotes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.overview_list_item, null);
        TextView note_title = rowView.findViewById(R.id.overview_note_title);
        TextView note_description = rowView.findViewById(R.id.overview_note_description);
        TextView note_timestamp = rowView.findViewById(R.id.overview_note_timestamp);

        Note note = mAllNotes.get(position);

        note_title.setText(note.getTitle());
        note_description.setText(note.getContent());
        note_timestamp.setText(TIME_FORMATTER.format(note.getLastModification()));

        return rowView;
    }
}