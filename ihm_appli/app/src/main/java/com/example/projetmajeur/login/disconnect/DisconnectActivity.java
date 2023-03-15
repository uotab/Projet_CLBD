package com.example.projetmajeur.login.disconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetmajeur.R;

import org.json.JSONException;

import java.io.IOException;

public class DisconnectActivity extends AppCompatActivity {

    private Button disconnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnect);

        disconnectButton = findViewById(R.id.buttonDisconnect);

        disconnectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
                String username = sharedPref.getString("username", null);
                System.out.println(username+"______________________________________");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();
                finish();
            }
        });
    }



}
