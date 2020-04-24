package com.example.note.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.example.note.R;
import com.example.note.fragments.NoteOverviewFragment;
import com.example.note.model.Note;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterDialog extends Dialog implements android.view.View.OnClickListener {
    private NoteOverviewFragment mFragment;
    private Button mButtonYes;
    private Button mButtonNo;
    private ChipGroup mChipGroup;
    private Map<Integer, String> mTags;
    private String mSelectedTag;

    public FilterDialog(NoteOverviewFragment fragment) {
        super(fragment.getActivity());
        this.mFragment = fragment;
        this.mTags = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_dialog);
        mButtonYes = (Button) findViewById(R.id.btn_yes);
        mButtonNo = (Button) findViewById(R.id.btn_no);
        mChipGroup = (ChipGroup) findViewById(R.id.filter_chips);
        mButtonYes.setOnClickListener(this);
        mButtonNo.setOnClickListener(this);

        getAllTags();

        mChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                mSelectedTag = mTags.get(i);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                mFragment.filterByTag(mSelectedTag);
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void getAllTags() {
        List<Note> notes = mFragment.getAllNotes();
        for(Note note : notes) {
            Chip chip;
            if(!mTags.containsValue(note.getTag())) {
                chip = (Chip) getLayoutInflater().inflate(R.layout.chip, mChipGroup, false);
                chip.setText(note.getTag());
                mChipGroup.addView(chip);
                mTags.put(chip.getId(), note.getTag());
            }
        }
    }
}
