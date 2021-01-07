package com.bonniepeng.bloomi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPass;
    private Button btnLogin, btnSignup, forgotPass;
    private TextView txtError;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        instantiate();

        // LOGIN
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();
                txtError.setVisibility(View.INVISIBLE);

                if (!email.equals("")) {
                    if (!password.equals("")) {
                        // Firebase's log in method from Docs
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            btnLogin.setEnabled(false);
                                            Log.i("LOGIN", "signInWithEmail:success");

                                            // Store the password
                                            // TODO: possibly make this more secure?
                                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("password", edtPass.getText().toString());
                                            editor.apply();

                                            // Go to Home
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);

                                        } else {

                                            // If sign in fails, display a message to the user.
                                            try {
                                                throw Objects.requireNonNull(task.getException());

                                            } catch (FirebaseAuthInvalidUserException e) {
                                                loginFail("Incorrect email.");

                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                loginFail("Incorrect email or password.");

                                            } catch (Exception e) {
                                                loginFail("Could not log in. Please try again later.");
                                                Log.e("LOG IN", Objects.requireNonNull(e.getMessage()));
                                            }
                                        }
                                    }
                                });
                    } else {
                        loginFail("Incorrect password.");
                    }
                } else {
                    loginFail("Email cannot be blank.");
                }
            }
        });


        // FORGOT PASSWORD
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ForgotPassDialog fpd = new ForgotPassDialog(LoginActivity.this);
                InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                fpd.getWindow().setBackgroundDrawable(inset);
                fpd.show();

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

    private void loginFail(String message) {

        txtError.setText(message);
        txtError.setVisibility(View.VISIBLE);

    }


    private void instantiate() {
        // EDITTEXTS
        edtEmail = findViewById(R.id.edtLIEmail);
        edtPass = findViewById(R.id.edtLIPass);

        // BUTTONS
        btnLogin = findViewById(R.id.btnLILogin);
        btnSignup = findViewById(R.id.btnSignUp);
        forgotPass = findViewById(R.id.btnForgotPass);

        // TEXTVIEWS
        txtError = findViewById(R.id.txtError);

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();

    }

    // CHECKING IF USER IS ALREADY SIGNED IN

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

}
