package com.example.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.note.controller.NoteManager;
import com.example.note.db.DatabaseHelper;
import com.example.note.db.GetNotesTask;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private NoteManager noteManager = NoteManager.getInstance();

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
                            noteManager.emptyNotes();
                        }
                    })
                    .show();
            return true;
        }

        if (id == R.id.action_export_notes) {
            Toast.makeText(this, "Export Notes to Device...", Toast.LENGTH_LONG).show();
            // export data to device
            noteManager.exportNotes(this);
            Toast.makeText(this, "Stored Notes to Device", Toast.LENGTH_LONG).show();
        }

        if (id == R.id.action_import_notes) {
            File dir = this.getFilesDir();

            final String[] files = dir.list();
            Arrays.sort(files, Collections.reverseOrder());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select File");
            builder.setItems(files, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String fileName = files[i];
                    for(File file : getApplicationContext().getFilesDir().listFiles()) {
                        if (file.getName().equals(fileName)) {
                            noteManager.importNotes(file);
                        }
                    }

                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
