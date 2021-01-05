package com.bonniepeng.bloomi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText edtEmail, edtPass1, edtPass2;
    private Button signUp;
    private TextView invalidEmail, invalidPass1, invalidPass2;
    private FirebaseAuth mAuth;


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

                mAuth.createUserWithEmailAndPassword(email, pass1)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNUP", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGNUP", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();

    }
}