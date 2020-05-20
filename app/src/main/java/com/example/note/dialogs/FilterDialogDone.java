package com.example.note.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.fragments.DoneNotesOverviewFragment;
import com.example.note.model.Note;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterDialogDone extends Dialog implements View.OnClickListener {
    private DoneNotesOverviewFragment mFragment;
    private Button mButtonYes;
    private Button mButtonNo;
    private Button mButtonFrom;
    private Button mButtonTo;
    private TextView mFromText;
    private TextView mToText;
    private ChipGroup mChipGroup;
    private Map<Integer, String> mTags;
    private String mSelectedTag;
    private Long fromMilliseconds = null;
    private Long toMilliseconds = null;

    DateFormat df = DateFormat.getDateInstance();

    public FilterDialogDone(DoneNotesOverviewFragment fragment) {
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
        mButtonTo = findViewById(R.id.btn_to);
        mButtonFrom = findViewById(R.id.btn_from);
        mChipGroup = (ChipGroup) findViewById(R.id.filter_chips);
        mFromText = findViewById(R.id.from_text);
        mToText = findViewById(R.id.to_text);
        mButtonYes.setOnClickListener(this);
        mButtonNo.setOnClickListener(this);

        getAllTags();

        mChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                mSelectedTag = mTags.get(i);
            }
        });
        String pattern = "dd.MM.yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog  fromTime = new DatePickerDialog(mFragment.getContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth,0,0,0);
                fromMilliseconds = newDate.getTimeInMillis();
                mFromText.setText(dateFormat.format(fromMilliseconds));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog  toTime = new DatePickerDialog(mFragment.getContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth,23,59,59);
                toMilliseconds = newDate.getTimeInMillis();
                mToText.setText(dateFormat.format(toMilliseconds));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mButtonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromTime.show();
            }
        });

        mButtonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTime.show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                mFragment.filterByTagAndDate(mSelectedTag, fromMilliseconds, toMilliseconds);
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
