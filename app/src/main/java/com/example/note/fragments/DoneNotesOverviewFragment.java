package com.example.note.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.note.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DoneNotesOverviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.done_notes_overview_fragment, container, false);

        FloatingActionButton back_all_notes_btn = view.findViewById(R.id.overview_back_all_notes_btn);
        back_all_notes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Showing All Notes",
                        Toast.LENGTH_LONG).show();

                NavHostFragment.findNavController(DoneNotesOverviewFragment.this)
                        .navigate(R.id.action_done_overview_to_overview_fragment);
            }
        });
        return view;
    }
}
