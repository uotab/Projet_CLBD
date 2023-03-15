package com.example.projetmajeur;

import java.io.Serializable;

public class Song implements Serializable {

    private long id;
    private String title;
    private String artist;
    private String style;
    private String name;
    private String path;



    public Song(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.style = null;
        this.name = null;
        this.path = null;
    }
    public Song(long id, String title, String artist, String style, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.style = style;
        this.name = null;
        this.path = path;
    }

    public Song(String name, String path) {
        this.id = 0;
        this.title = null;
        this.artist = null;
        this.style = null;
        this.name = name;
        this.path = path;
    }

    public Song(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getTitle() { return title; }

    public String getArtist() { return artist; }

    public String getStyle() {
        return style;
    }

    public String getPath() { return path; }


}
