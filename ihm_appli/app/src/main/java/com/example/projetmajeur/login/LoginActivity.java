package com.example.projetmajeur.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import androidx.appcompat.app.AppCompatActivity;

import com.example.projetmajeur.R;
import com.example.projetmajeur.register.RegisterActivity;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterPage();
            }
        });
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    userLogin();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void userLogin() throws IOException, JSONException {
        EditText textUsername = findViewById(R.id.editUsername);
        String username = textUsername.getText().toString();
        EditText textPassword = findViewById(R.id.editPassword);
        String pwd = textPassword.getText().toString();
        LoginParams params = new LoginParams(pwd,username);
        new LoginNetworkRequest(this).execute(params);
    }
    private void goToRegisterPage() {
        // start the recording activity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
