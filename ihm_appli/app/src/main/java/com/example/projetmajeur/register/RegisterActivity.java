package com.example.projetmajeur.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetmajeur.R;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });
    }


    private void userRegister() {
        EditText textUsername = findViewById(R.id.editUsername);
        String username = textUsername.getText().toString();
        EditText textPassword = findViewById(R.id.editPassword);
        String pwd = textPassword.getText().toString();
        EditText textLastName = findViewById(R.id.editLastName);
        String lastname = textLastName.getText().toString();
        EditText textSurName = findViewById(R.id.editFirstName);
        String surname = textSurName.getText().toString();
        EditText textEmail = findViewById(R.id.editEmail);
        String email = textEmail.getText().toString();
        RegisterParams params = new RegisterParams(pwd,username,surname,lastname,email);
        new RegisterNetworkRequest(this).execute(params);

    }
}
