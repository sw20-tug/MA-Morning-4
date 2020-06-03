package com.example.note.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.note.R;
import com.example.note.controller.NoteManager;
import com.example.note.model.Note;

public class NoteDetailFragment extends Fragment {

    private TextView nNoteTitle;
    private TextView nNoteDescription;
    private TextView nNoteTag;
    private NoteManager nNoteManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        nNoteTitle = view.findViewById(R.id.detail_note_title);
        nNoteDescription = view.findViewById(R.id.detail_note_description);
        nNoteTag = view.findViewById(R.id.detail_note_tag);

        nNoteManager = NoteManager.getInstance();

        if(getArguments() == null)
            return view;

        int note_id = getArguments().getInt("note_id", -1);

        final Note note = nNoteManager.getNoteById(note_id);

        if (note != null) {
            nNoteTitle.setText(note.getTitle());
            nNoteDescription.setText(note.getContent());
            nNoteTag.setText(note.getTag());
        }

        Button saveNoteBtn = view.findViewById(R.id.save_note_btn);
        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = 0;
                if(note == null) {
                    result = nNoteManager.addNote(
                            nNoteTitle.getText().toString(),
                            nNoteDescription.getText().toString(),
                            nNoteTag.getText().toString()
                    );
                } else {
                    note.setTitle(nNoteTitle.getText().toString());
                    note.setContent(nNoteDescription.getText().toString());
                    note.setTag(nNoteTag.getText().toString());
                    nNoteManager.updateNote(note, true);
                }
                if(result != 0) {

                    for(int i = 0; i < 2; i++){
                        Toast noTitle = Toast.makeText(getContext(), "Title must have at least 3 characters!", Toast.LENGTH_LONG);
                        noTitle.getView().getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        noTitle.setGravity(Gravity.TOP|Gravity.CENTER, 0, 100);
                        noTitle.show();
                    }
                } else {
                    NavHostFragment.findNavController(NoteDetailFragment.this)
                            .navigate(R.id.action_detail_to_overview_fragment, null);
                }

            }
        });

        return view;
    }
}
