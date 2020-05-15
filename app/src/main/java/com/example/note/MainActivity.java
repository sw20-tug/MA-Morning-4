package com.example.note;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.note.controller.NoteManager;
import com.example.note.db.AppDatabase;
import com.example.note.db.DatabaseHelper;
import com.example.note.db.GetNotesTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper.getInstance().initDb(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetNotesTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_empty_notes) {
            Bundle bundle = new Bundle();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Empty Notes")
                    .setMessage("Are you sure you want to delete all stored notes?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NoteManager manager = NoteManager.getInstance();
                            manager.emptyNotes();
                        }
                    })
                    .show();
            return true;
        }

        if (id == R.id.action_export_notes) {
            Toast.makeText(this, "Export Notes to Device...", Toast.LENGTH_LONG).show();
            // export data to device
            NoteManager noteManager = NoteManager.getInstance();
            noteManager.exportNotes(this);
            Toast.makeText(this, "Stored Notes to Device", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
