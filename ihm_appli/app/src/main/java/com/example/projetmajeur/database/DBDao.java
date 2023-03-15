package com.example.projetmajeur.database;

import android.content.Context;

import com.example.projetmajeur.Song;

import java.util.List;

public class DBDao{

    private BDD db;
    private static DBDao instance;

    public DBDao(Context context){
        db = new BDD(context);
    }

    public static DBDao getInstance(Context context) {
        if (instance == null){
            instance = new DBDao(context);
        }
        return instance;
    }

    public void addUser(int id){
        db.addUser(id);
        return;
    }

    public void addSong(String title,String artist, String style, String filePath ,int id){
        db.addSong(title, artist, style, filePath, id);
        return;
    }

    public List<Song> getSongForUser(int id){
        return db.getSongForUser(id);
    }

    public void addRecoSongs(String pathToSongs){
        db.addRecoSongs(pathToSongs);
    }

    public List<Song> getRecoSong(List recoListRequest){
        return db.getRecoSongs(recoListRequest);
    }

    public void addRecommendedSong(String name, String title, String artist, String style, String filePath){ db.addRecommendedSong(name, title, artist, style, filePath);}

    public List<Song> getNameSong(int idMusic){ return db.getNameSong(idMusic);}

    public int getSongId(String title, String artist, String style, String filePath) {return db.getSongId(title, artist, style, filePath);}
}
