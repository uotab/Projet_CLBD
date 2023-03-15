package com.example.projetmajeur;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class SongViewModel extends BaseObservable {
    private Song song;
    public void setSong(Song song) {
        this.song = song;
        notifyChange();
    }

    @Bindable
    public String getTitle() {
        return song.getTitle();
    }
    @Bindable
    public String getArtist() {
        return song.getArtist();
    }
    @Bindable
    public String getStyle() {
        return song.getStyle();
    }
    @Bindable
    public String getPath() { return song.getPath(); }

}
