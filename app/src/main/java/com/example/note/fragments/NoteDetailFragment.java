package com.example.note.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.note.R;
import com.example.note.controller.NoteManager;
import com.example.note.model.Note;

public class NoteDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        NoteManager note_manager = NoteManager.getInstance();

        if(getArguments() == null)
            return view;

        int note_id = getArguments().getInt("note_id", -1);

        if (note_id == -1)
            return view;

        Note note = note_manager.getNodeById(note_id);

        if (note == null)
            return view;

        TextView note_title = view.findViewById(R.id.detail_note_title);
        TextView note_description = view.findViewById(R.id.detail_note_description);

        note_title.setText(note.getTitle());
        note_description.setText(note.getContent());

        return view;
    }
}
