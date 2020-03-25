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

    private TextView nNoteTitle;
    private TextView nNoteDescription;
    private NoteManager nNoteManager;
    private Note nNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        nNoteTitle = view.findViewById(R.id.detail_note_title);
        nNoteDescription = view.findViewById(R.id.detail_note_description);

        nNoteManager = NoteManager.getInstance();

        if(getArguments() == null)
            return view;

        int note_id = getArguments().getInt("note_id", -1);

        if (note_id == -1)
            return view;

        nNote = nNoteManager.getNodeById(note_id);

        if (nNote == null)
            return view;

        nNoteTitle.setText(nNote.getTitle());
        nNoteDescription.setText(nNote.getContent());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(nNoteTitle != null && nNoteDescription != null && nNoteManager != null && nNote != null) {
            String newTitle = nNoteTitle.getText().toString();
            String newContent = nNoteDescription.getText().toString();

            if(!nNote.getTitle().equals(newTitle) || !nNote.getContent().equals(newContent))
                nNote.setLastModification(System.currentTimeMillis());

            nNote.setTitle(newTitle);
            nNote.setContent(newContent);
        }
    }
}
