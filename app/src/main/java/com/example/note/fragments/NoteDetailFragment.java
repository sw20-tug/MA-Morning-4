package com.example.note.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.note.R;
import com.example.note.controller.NoteManager;
import com.example.note.model.Note;

public class NoteDetailFragment extends Fragment {

    private TextView nNoteTitle;
    private TextView nNoteDescription;
    private NoteManager nNoteManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        nNoteTitle = view.findViewById(R.id.detail_note_title);
        nNoteDescription = view.findViewById(R.id.detail_note_description);

        nNoteManager = NoteManager.getInstance();

        if(getArguments() == null)
            return view;

        int note_id = getArguments().getInt("note_id", -1);

        final Note note = nNoteManager.getNodeById(note_id);

        if (note != null) {
            nNoteTitle.setText(note.getTitle());
            nNoteDescription.setText(note.getContent());
        }

        Button saveNoteBtn = view.findViewById(R.id.save_note_btn);
        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(note == null) {
                    nNoteManager.addNote(
                            nNoteTitle.getText().toString(),
                            nNoteDescription.getText().toString()
                    );
                } else {
                    note.setTitle(nNoteTitle.getText().toString());
                    note.setContent(nNoteDescription.getText().toString());
                    nNoteManager.updateNote(note);
                }
                NavHostFragment.findNavController(NoteDetailFragment.this)
                        .navigate(R.id.action_detail_to_overview_fragment, null);
            }
        });

        return view;
    }
}
