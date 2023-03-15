package com.example.projetmajeur;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.projetmajeur.database.DBDao;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordingActivity extends AppCompatActivity  {

    private static final int RECORDING_DURATION = 10000; // 10 seconds



    private MediaRecorder mediaRecorder;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    private Button stopButton;
    private DBDao db;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    String fileName = df.format(new Date()) + ".mpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        db = DBDao.getInstance(this.getApplicationContext());

        // find the views
        timerTextView = findViewById(R.id.timer_text_view);
        stopButton = findViewById(R.id.stop_button);

        // set up the stop button click listener
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stopRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // start the recording countdown timer
        countDownTimer = new CountDownTimer(RECORDING_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.format("%d:%02d", millisUntilFinished / 60000, (millisUntilFinished % 60000) / 1000));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
                try {
                    stopRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // initialize the media recorder
        releaseMediaRecorder();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = df.format(new Date()) + ".mpeg";
        String filePath = getExternalFilesDir(null) + fileName;
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        // prepare the media recorder
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // start the media recorder

        mediaRecorder.start();
    }


    private void stopRecording() throws IOException {
        // stop the media recorder
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            String filePath = getExternalFilesDir(null) + fileName;
            new SongIdentifier(new SongIdentifier.OnIdentificationCompleteListener() {
                @Override
                public void onIdentificationComplete(String title, String artist) {
                    // Update the UI to display the identified song information
                    TextView timerTextView = findViewById(R.id.timer_text_view);
                    timerTextView.setVisibility(View.INVISIBLE);
                    Button stopButton = findViewById(R.id.stop_button);
                    stopButton.setVisibility(View.INVISIBLE);
                    TextView identifiedSongTextView = findViewById(R.id.identified_song_text_view);
                    SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    int id = sharedPref.getInt("id",0);
                    //db.addSong(title,artist,"Pop",filePath,id);
                    System.out.println(db.getSongForUser(id));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            identifiedSongTextView.setText(String.format("Identified song: %s by %s", title, artist));

                            // Add a button to return to the main activity
                            Button returnButton = findViewById(R.id.return_button);
                            returnButton.setVisibility(View.VISIBLE);
                            returnButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        }
                    });
                    new StyleIdentifier(new StyleIdentifier.OnIdentificationCompleteListener() {
                        @Override
                        public void onIdentificationComplete(String style, String reco1, String reco2) {
                            // Update the UI to display the identified style information
                            TextView identifiedStyleTextView = findViewById(R.id.identified_style_text_view);
                            TextView recommendationsTextView = findViewById(R.id.recommendations_text_view);
                            TextView recommendation1TextView = findViewById(R.id.recommendation1_text_view);
                            TextView recommendation2TextView = findViewById(R.id.recommendation2_text_view);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                                    int id = sharedPref.getInt("id",0);
                                    db.addSong(title,artist,style,filePath,id);
                                    db.addRecommendedSong(reco1, title, artist, style, filePath);
                                    db.addRecommendedSong(reco2, title, artist, style, filePath);
                                    recommendationsTextView.setVisibility(View.VISIBLE);
                                    identifiedStyleTextView.setText(String.format("The style is %s", style));
                                    recommendation1TextView.setText(reco1);
                                    recommendation2TextView.setText(reco2);

                                }
                            });
                        }


                        @Override
                        public void onIdentificationError(String errorMessage) {
                            // Update the UI to display the error

                            TextView identifiedStyleTextView = findViewById(R.id.identification_style_error_text_view);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    identifiedStyleTextView.setText(String.format("An error occurred: %s", errorMessage));


                                }
                            });
                        }
                    }).execute(filePath);
                }


                @Override
                public void onIdentificationError(String errorMessage) {
                    // Update the UI to display the error
                    TextView timerTextView = findViewById(R.id.timer_text_view);
                    timerTextView.setVisibility(View.INVISIBLE);
                    Button stopButton = findViewById(R.id.stop_button);
                    stopButton.setVisibility(View.INVISIBLE);
                    TextView identifiedSongTextView = findViewById(R.id.identification_error_text_view);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            identifiedSongTextView.setText(String.format("An error occurred: %s", errorMessage));

                            // Add a button to return to the main activity
                            Button returnButton = findViewById(R.id.return_button);
                            returnButton.setVisibility(View.VISIBLE);
                            returnButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });

                        }
                    });
                }
            }).execute(filePath);

            /*File file = new File(filePath);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);
            inputStream.close();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"lucassavy@orange.fr"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "fichier");
            emailIntent.putExtra(Intent.EXTRA_STREAM, getUriForFile(this, file));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));*/


        }
        else{

            // stop the countdown timer
            countDownTimer.cancel();
            // return to the main activity
            finish();
        }




    }
    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            // clear recorder configuration
            mediaRecorder.reset();
            // release the recorder object
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    private Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, "com.example.projetmajeur.fileprovider", file);
    }




}


