package com.example.projetmajeur;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetmajeur.database.DBDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class SongDetailsActivity extends AppCompatActivity {

    private DBDao db;
    private boolean isPlaying = false;
    private static final String API_URL = "http://54.162.139.106:5000/audio";
    private static final String TAG = SongIdentifier.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);
        db = DBDao.getInstance(this.getApplicationContext());
        Song song = (Song)getIntent().getSerializableExtra("song");
        List nameSongs = db.getNameSong(db.getSongId(song.getTitle(), song.getArtist(), song.getStyle(), song.getPath()));
        String name1 = nameSongs.get(0).toString();
        String name2 = nameSongs.get(0).toString();

        TextView songTextView = findViewById(R.id.song_text_view);
        songTextView.setText(song.getTitle());

        TextView artistTextView = findViewById(R.id.artist_text_view);
        artistTextView.setText(song.getArtist());

        TextView styleTextView = findViewById(R.id.style_text_view);
        styleTextView.setText(String.format("Style: %s",song.getStyle()));

        TextView reco1TextView = findViewById(R.id.reco1_text_view);
        reco1TextView.setText(String.format("First recommendation: %s", nameSongs.get(0)));

        TextView reco2TextView = findViewById(R.id.reco2_text_view);
        reco2TextView.setText(String.format("Second recommendation: %s", nameSongs.get(0)));

        /*// Create a MediaPlayer object using the file path of the song
        MediaPlayer mediaPlayer1 = new MediaPlayer();
        MediaPlayer mediaPlayer2 = new MediaPlayer();
        try {
            mediaPlayer1.setDataSource(String.valueOf(new GetAudioRecommendedSongs().execute(name1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer2.setDataSource(String.valueOf(new GetAudioRecommendedSongs().execute(name2)));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            mediaPlayer1.prepare();
            mediaPlayer2.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button playButton1 = findViewById(R.id.play_button_1);
        playButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying){
                    mediaPlayer1.start();
                    playButton1.setText("Pause");
                    isPlaying = true;
                }
                else{
                    mediaPlayer1.pause();
                    playButton1.setText("Start");
                    isPlaying = false;
                }
            }
        });

        Button playButton2 = findViewById(R.id.play_button_2);
        playButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying){
                    mediaPlayer2.start();
                    playButton2.setText("Pause");
                    isPlaying = true;
                }
                else{
                    mediaPlayer2.pause();
                    playButton2.setText("Start");
                    isPlaying = false;
                }

            }
        });



        SeekBar seekBar1 = findViewById(R.id.seekbar_1);
        seekBar1.setMax(mediaPlayer1.getDuration());
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer1.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seekBar2 = findViewById(R.id.seekbar_2);
        seekBar2.setMax(mediaPlayer2.getDuration());
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer2.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }
     /*public byte[] getRecommendedSong(String name) {

         byte[] audioData = new byte[0];
         try {

             // Create the URL
             URL url = new URL(API_URL);

             // Open a connection
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();

             // Set the request method to POST
             conn.setRequestMethod("POST");

             // Set the request properties
             conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             conn.setDoOutput(true);

             // Create the request body
             String requestBody = "string1=" + URLEncoder.encode(name, "UTF-8");

             // Send the request
             OutputStream os = conn.getOutputStream();
             os.write(requestBody.getBytes());
             os.flush();
             os.close();

             // Get the response code
             int responseCode = conn.getResponseCode();

             // If the response is successful (200)
             if (responseCode == HttpURLConnection.HTTP_OK) {
                 InputStream is = conn.getInputStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 byte[] buffer = new byte[1024];
                 int len;
                 while ((len = is.read(buffer)) != -1) {
                     baos.write(buffer, 0, len);
                 }
                 audioData = baos.toByteArray();

             } else {
                 // Handle unsuccessful response
                 Log.e(TAG, "Failed to identify style: HTTP error code " + responseCode);
             }
         } catch (IOException e) {
             e.printStackTrace();
         }


         return audioData;
     }*/
}
