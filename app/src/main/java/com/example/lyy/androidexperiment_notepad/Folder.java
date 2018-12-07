package com.example.lyy.androidexperiment_notepad;

import java.util.List;

public class Folder {
    private int id;
    private String title;
    //    private List<Note> notes;
    private int note_num;

    public int getNote_num() {
        return note_num;
    }

    public void setNote_num(int note_num) {
        this.note_num = note_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public List<Note> getNotes() {
//        return notes;
//    }
//
//    public void setNotes(List<Note> notes) {
//        this.notes = notes;
//    }
}
