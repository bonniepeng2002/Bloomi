package com.bonniepeng.bloomi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText edtEmail, edtPass1, edtPass2;
    private Button signUp;
    private TextView invalidEmail, invalidPass1, invalidPass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        instantiate();

        String email = edtEmail.getText().toString();
        String pass1 = edtPass1.getText().toString();
        String pass2 = edtPass2.getText().toString();

        // SIGNING UP
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: if email is already in db
                // TODO: if passwords don't match
                // TODO: add to db and proceed

                finish();
            }
        });

    }

    private void instantiate() {
        // EDITTEXTS
        edtEmail = findViewById(R.id.edtSUEmail);
        edtPass1 = findViewById(R.id.edtSUPass);
        edtPass2 = findViewById(R.id.edtSUPass2);

        // BUTTON
        signUp = findViewById(R.id.btnSUSignUp);

        // TEXTVIEWS
        invalidEmail = findViewById(R.id.emailInvalid);
        invalidPass1 = findViewById(R.id.passInvalid1);
        invalidPass2 = findViewById(R.id.passInvalid2);

    }
}