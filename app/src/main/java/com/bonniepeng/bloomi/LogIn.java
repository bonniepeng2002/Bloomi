package com.bonniepeng.bloomi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    private EditText edtEmail, edtPass;
    private Button btnLogin, btnSignup;
    private TextView invalidEmail, invalidPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instantiate();

        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();

        // LOGIN
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: email exists
                // TODO: password is correct
            }
        });

        // SIGNUP
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });

    }

    private void instantiate() {
        // EDITTEXTS
        edtEmail = findViewById(R.id.edtSUEmail);
        edtPass = findViewById(R.id.edtSUPass);

        // BUTTONS
        btnLogin = findViewById(R.id.btnSUSignUp);
        btnSignup = findViewById(R.id.btnSignUp);

        // TEXTVIEWS
        invalidEmail = findViewById(R.id.falseEmail);
        invalidPass = findViewById(R.id.falsePass);
    }

}
