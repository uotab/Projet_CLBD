package com.example.projetmajeur.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projetmajeur.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BDD extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Muzik";

    //user
    private static final String TABLE_USER = "Users";
    private static final String UID = "_idU";


    //song
    private static final String TABLE_SONG = "Songs";
    private static final String MID = "_idM";
    private static final String SINGER = "Singer";
    private static final String SONG = "Song";
    private static final String STYLE = "Style";
    private static final String PATH = "Path";

    //listening songs
    private static final String TABLE_LISTENING_SONG = "Listening_songs";
    private static final String LSID = "_idLS";
    private static final String USER_ID = "id_user";
    private static final String SONG_ID = "id_song";

    //recommendation songs
    private static final String TABLE_RECOMMENDATION_SONG = "Recommendation_songs";
    private static final String RSID = "_idRS";
    private static final String RECO_NAME = "Reco_name";
    private static final String RECO_PATH = "Reco_path";

    //recommended songs
    private static final String TABLE_RECOMMENDED_SONG = "Recommended_songs";
    private static final String RDSID = "_idRDS";
    private static final String NAME = "Name";
    private static final String MUSIC_ID = "id_music";

    public BDD(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER +" ("
                + UID + " INTEGER PRIMARY KEY);";
        db.execSQL(CREATE_TABLE_USER);

        String CREATE_TABLE_SONG = "CREATE TABLE " + TABLE_SONG +" ("
                + MID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SINGER + " VARCHAR(255),"
                + SONG + " VARCHAR(255),"
                + STYLE + " VARCHAR(255),"
                + PATH + " VARCHAR(255));";

        db.execSQL(CREATE_TABLE_SONG);

        String CREATE_TABLE_LISTENING_SONG = "CREATE TABLE " + TABLE_LISTENING_SONG +" ("
                + LSID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " int,"
                + SONG_ID + " int,"
                + " FOREIGN KEY (" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + UID + "), "
                + " FOREIGN KEY (" + SONG_ID + ") REFERENCES " + TABLE_SONG + "(" + MID + ")); ";
        db.execSQL(CREATE_TABLE_LISTENING_SONG);

        String CREATE_TABLE_RECOMMENDATION_SONG = "CREATE TABLE " + TABLE_RECOMMENDATION_SONG +" ("
                + RSID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RECO_NAME + " VARCHAR(255),"
                + RECO_PATH + " VARCHAR(255));";

        db.execSQL(CREATE_TABLE_RECOMMENDATION_SONG);

        String CREATE_TABLE_RECOMMENDED_SONG = "CREATE TABLE " + TABLE_RECOMMENDED_SONG + " ("
                + RDSID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " VARCHAR(255),"
                + MUSIC_ID + " VARCHAR(255),"
                + " FOREIGN KEY (" + MUSIC_ID + ") REFERENCES " + TABLE_SONG + "(" + MID + ")); ";
        db.execSQL(CREATE_TABLE_RECOMMENDED_SONG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTENING_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDATION_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDED_SONG);
        onCreate(db);
    }


    public void addUser(int id){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UID, id);
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public void addSong(String title, String artist, String style, String filePath, int idUser){
        db = this.getWritableDatabase();
        String[] columns = null;
        String selection = SONG + "=? AND "+SINGER+"=? AND "+STYLE+"=? AND "+PATH+"=?";
        String[] selectionArgs = {title,artist,style, filePath};

        ContentValues values = new ContentValues();
        values.put(SINGER, artist);
        values.put(SONG, title);
        values.put(STYLE, style);
        values.put(PATH, filePath);
        db.insert(TABLE_SONG, null, values);
        db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_SONG, null,selection,selectionArgs,null,null,null);
        c.moveToFirst();
        int idMusic = c.getInt(c.getColumnIndexOrThrow("_idM"));
        addSongForUser(idMusic,idUser);
        c.close();
        db.close();
    }

    public int getSongId(String title, String artist, String style, String filePath) {
        db = this.getReadableDatabase();
        String[] columns = null;
        String selection = SONG + "=? AND "+SINGER+"=? AND "+STYLE+"=? AND "+PATH+"=?";
        String[] selectionArgs = {title,artist,style, filePath};
        Cursor c = db.query(TABLE_SONG, null,selection,selectionArgs,null,null,null);
        c.moveToFirst();
        int idMusic = c.getInt(c.getColumnIndexOrThrow("_idM"));
        c.close();
        return idMusic;

    }
    public void addSongForUser(int idMusic, int idUser){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, idUser);
        values.put(SONG_ID, idMusic);
        db.insert(TABLE_LISTENING_SONG, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<Song> getSongForUser(int idUser){
        db = this.getReadableDatabase();
        String[] columns = null;
        String selection = USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(idUser)};
        Cursor c = db.query(TABLE_LISTENING_SONG, columns,selection,selectionArgs,null,null,null);
        List<Song> songList = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                int idMusic = c.getInt(c.getColumnIndex(SONG_ID));
                String[] col = null;
                String select = MID + "=?";
                String[] selectArgs = {String.valueOf(idMusic)};
                Cursor cursor = db.query(TABLE_SONG, col,select,selectArgs,null,null,null);
                cursor.moveToFirst();
                songList.add(new Song(idMusic,cursor.getString(cursor.getColumnIndex(SONG)),cursor.getString(cursor.getColumnIndex(SINGER)),cursor.getString(cursor.getColumnIndex(STYLE)), cursor.getString(cursor.getColumnIndex(PATH))));
            } while
            (c.moveToNext());
        }

        c.close();
        db.close();

        return songList;
    }

    public void addRecoSongs(String pathToSongs){
        db = this.getWritableDatabase();
        File directory = new File(pathToSongs);
        File[] files = directory.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            String filePath = file.getAbsolutePath();
            ContentValues values = new ContentValues();
            values.put(RECO_NAME, fileName);
            values.put(RECO_PATH, filePath);
            db.insert(TABLE_RECOMMENDATION_SONG, null, values);
        }
        db.close();
    }

    @SuppressLint("Range")
    public List<Song> getRecoSongs(List recoListRequest){
        db = this.getReadableDatabase();
        List<Song> recoList = new ArrayList<>();
        for (Object name : recoListRequest){
            String [] columns = null;
            String selection = RECO_NAME + "=?";
            String[] selectionArgs = {String.valueOf(name)};
            Cursor cursor = db.query(TABLE_RECOMMENDATION_SONG, columns,selection,selectionArgs,null,null,null);
            recoList.add(new Song(name.toString(),cursor.getString(cursor.getColumnIndex(RECO_PATH))));
        }
        db.close();

        return recoList;
    }

    @SuppressLint("Range")
    public void addRecommendedSong(String name, String title, String artist, String style, String filePath){
        db = this.getWritableDatabase();
        int idRecommended = getSongId(title, artist, style, filePath);
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(MUSIC_ID, idRecommended);
        db.insert(TABLE_RECOMMENDED_SONG, null, values);
        db.close();
    }



    @SuppressLint("Range")
    public List<Song> getNameSong(int idMusic){
        db = this.getReadableDatabase();
        String[] columns = null;
        String selection = MUSIC_ID + "=?";
        String[] selectionArgs = {String.valueOf(idMusic)};
        List<Song> nameSongs = new ArrayList<>();
        Cursor c = db.query(TABLE_RECOMMENDED_SONG, columns,selection,selectionArgs,null,null,null);
        c.moveToFirst();
        nameSongs.add(new Song(c.getString(c.getColumnIndex(NAME))));
        return nameSongs;
    }

    /*public List<Song> getPathSong(String nameMusic){

    }*/

}
