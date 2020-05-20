package com.example.note.controller;

import android.content.Context;

import com.example.note.db.DeleteNotesTask;
import com.example.note.db.EmptyNotesTableTask;
import com.example.note.db.InsertNotesTask;
import com.example.note.db.UpdateNotesTask;
import com.example.note.model.Note;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

public class NoteManager extends Observable {
    private static NoteManager instance = null;

    private NoteManager() {
        notes = new ArrayList<>();
    };

    private List<Note> notes;

    public static NoteManager getInstance() {
        if(instance == null) {
            instance = new NoteManager();
        }
        return instance;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public int addNote(String title, String description, String tag) {
        if(title == null || title.length() < 3){
            return -1;
        }
        final Note note = new Note(getNextFreeId(), title, description, tag);
        notes.add(note);
        new InsertNotesTask().execute(note);
        return 0;
    }

    public int addNote(String title, String content, String tag, String sCreationTimestamp, String sLastModification, String sPinned, String sDone) {
        title = title.substring(1, title.length()-1);
        content = content.substring(1, content.length()-1);
        sCreationTimestamp = sCreationTimestamp.substring(1, sCreationTimestamp.length()-1);
        sLastModification = sLastModification.substring(1, sLastModification.length()-1);
        tag = tag.substring(1, tag.length()-1);
        Long creationTimestamp = Long.parseLong(sCreationTimestamp);
        Long lastModification = Long.parseLong(sLastModification);
        boolean pinned = Boolean.parseBoolean(sPinned.substring(1, sPinned.length()-1));
        boolean done = Boolean.parseBoolean(sDone.substring(1, sDone.length()-1));

        if(title == null || title.length() < 3) {
            return -1;
        }

        final Note note = new Note(getNextFreeId(), title, content, tag, creationTimestamp, lastModification, pinned, done, 0);
        notes.add(note);
        new InsertNotesTask().execute(note);
        return 0;
    }

    public Note getNoteById(int id) {
        for(Note note : notes) {
            if(note.getId() == id)
                return note;
        }
        return null;
    }

    public Integer getNextFreeId() {
        int id = 1;
        while(true) {
            if(getNoteById(id) == null)
                return id;
            id++;
        }
    }

    public void updateNote(Note note, boolean set_modification) {
        if(set_modification)
            note.setLastModification(System.currentTimeMillis());
        new UpdateNotesTask().execute(note);
    }

    public void deleteNote(Note note) {
        notes.remove(note);
        new DeleteNotesTask().execute(note);
    }

    public void emptyNotes() {
        notes.clear();
        notifyObservers(notes);
        new EmptyNotesTableTask().execute();
    }

    public void importNotes(File file) {
        List<List<String>> lines = new ArrayList<>();
        try{
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()) {
                String data = inputStream.nextLine();
                lines.add(Arrays.asList(data.split(",")));
            }
            inputStream.close();

            for(int i = 1; i < lines.size(); i++) {
                addNote(lines.get(i).get(0),
                        lines.get(i).get(1),
                        lines.get(i).get(2),
                        lines.get(i).get(3),
                        lines.get(i).get(4),
                        lines.get(i).get(5),
                        lines.get(i).get(6));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void exportNotes(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String fileName = "export_" + formatter.format(date);
        List<String[]> content = new ArrayList<String[]>();
        content.add(new String[] {"Title", "Content", "Tag", "creationTimestamp", "lastModification", "Pinned", "Done"});

        for(Note note : getNotes()) {
            content.add(new String[] {note.getTitle(),
                    note.getContent(),
                    note.getTag(),
                    note.getCreationTimestamp().toString(),
                    note.getLastModification().toString(),
                    note.isPinned() ? "True" : "False",
                    note.isMarkedAsDone() ? "True" : "False"});
        }

        writeFileToStorage(context, fileName, content);

    }

    public void writeFileToStorage(Context context, String fileName, List<String[]> content) {
        File file = new File(context.getFilesDir(),fileName + ".csv");

        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            writer.writeAll(content);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shareNote(int noteId) {}

    public void deleteTagOfNote(int i) {
        Note note = getNoteById(i);
        note.setTag("");
        updateNote(note, true);
    }
}
