package com.bonniepeng.bloomi;

import android.content.Intent;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPass;
    private Button btnLogin, btnSignup;
    private TextView invalidEmail, invalidPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instantiate();

        String type = "login";

        // LOGIN
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();

//                BackgroundWorker bkgr = new BackgroundWorker(LoginActivity.this);
//                bkgr.execute(type, email, password);

                // Firebase's log in method from Docs
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LOGIN", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Go to Home
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                                    Snackbar.make(view, "Incorrect email or password. Please try again.", BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        // SIGNUP
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
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

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();

    }

    // CHECKING IF USER IS ALREADY SIGNED IN
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Toast.makeText(this, "Already signed in!", Toast.LENGTH_SHORT).show();
            // TODO: redirect to user's dashboard
        } else {
            Toast.makeText(this, "Not yet signed in.", Toast.LENGTH_SHORT).show();
        }
    }

}
