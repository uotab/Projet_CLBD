package com.example.projetmajeur;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetmajeur.database.DBDao;
import com.example.projetmajeur.login.disconnect.DisconnectActivity;
import com.example.projetmajeur.login.LoginActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button recordButton;
    private ListView recentSongsList;
    private ImageView userIcon;
    private DBDao db;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private TextView usernameText;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_main);


        //database
        db = new DBDao(this);
        String path = String.valueOf(Environment.getExternalStorageDirectory()) + "/Android/data/com.example.projetmajeur/audio8sec";
        //db.addRecoSongs(path);
        // find the views
        usernameText = findViewById(R.id.username_text);
        recordButton = findViewById(R.id.record_button);
        userIcon = findViewById(R.id.user_icon);
        // set up the recent songs list adapter
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SongFragment fragment = new SongFragment();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();


        // set up the record button click listener
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                String username = sharedPref.getString("username", null);
                if (username != null) {
                    goToDisconnectPage();
                } else {
                    goToLoginPage();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String sessionToken = sharedPref.getString("session_token", null);
        if (sessionToken != null) {

        } else {

        }
        String username = sharedPref.getString("username", null);
        usernameText.setText(username);
    }

    private void startRecording() {
        // start the recording activity
        Intent intent = new Intent(this, RecordingActivity.class);
        startActivity(intent);
    }

    private void goToLoginPage() {
        // start the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToDisconnectPage() {
        // start the disconnect activity
        Intent intent = new Intent(this, DisconnectActivity.class);
        startActivity(intent);
    }


}
