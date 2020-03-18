package com.example.note.model;

import java.sql.Time;
import java.sql.Timestamp;

public class Note {
    private Integer id;
    private String title;
    private String content;
    private Timestamp creationTimestamp;
    private Timestamp lastModification;

    public Note(int id) {
        this.id = id;
        this.title = "";
        this.content = "";
        this.creationTimestamp = new Timestamp(System.currentTimeMillis());
        this.lastModification = this.creationTimestamp;
    }

    public Note(int id, String title) {
        this.id = id;
        this.title = title;
        this.content = "";
        this.creationTimestamp = new Timestamp(System.currentTimeMillis());
        this.lastModification = this.creationTimestamp;
    }

    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationTimestamp = new Timestamp(System.currentTimeMillis());
        this.lastModification = this.creationTimestamp;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public Timestamp getLastModification() {
        return lastModification;
    }

    public void setLastModification(Timestamp lastModification) {
        this.lastModification = lastModification;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", lastModification=" + lastModification +
                '}';
    }

    @Override
    public boolean equals(Object other)  {
        if (!(other instanceof Note)) {
            return false;
        }
        Note otherNote = (Note) other;

        if(this.id == otherNote.getId())
            return true;
        return false;
    }

}
