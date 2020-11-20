package com.example.listviewdemo.model;

import java.util.UUID;

public class Note {

    private String title;
    private String id = UUID.randomUUID().toString(); // default

    public Note(String title, String _id) {
        this.title = title;
        if(_id != null){
            id = _id;
        }
    }

    public Note(String title) { // brug denne når vi opretter ny Note fra Android
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }
}
