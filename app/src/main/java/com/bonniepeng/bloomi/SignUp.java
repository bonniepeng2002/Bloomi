package com.bonniepeng.bloomi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText edtEmail, edtPass1, edtPass2;
    private Button signUp;
    private TextView invalidEmail, invalidPass1, invalidPass2;
    private FirebaseAuth mAuth;
    private final String TAG = "SIGNUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        instantiate();

        // SIGNING UP
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: if email is already in db
                // TODO: if passwords don't match
                // TODO: add to db and proceed

                // getting input
                String email = edtEmail.getText().toString();
                String pass1 = edtPass1.getText().toString();
                String pass2 = edtPass2.getText().toString();
                invalidPass1.setVisibility(View.GONE);
                invalidPass2.setVisibility(View.GONE);
                invalidEmail.setVisibility(View.GONE);

                // creating user
                // from Firebase's Docs
                mAuth.createUserWithEmailAndPassword(email, pass1)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up success
                                    Log.w(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Go to Home
                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    try {
                                        throw Objects.requireNonNull(task.getException());

                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Snackbar.make(view, "Password must have at least 6 characters.", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        invalidPass(true);

                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Snackbar.make(view, "Please enter a valid email.", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        invalidemail(true);

                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Snackbar.make(view, "User has already registered.", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        invalidemail(true);

                                    } catch (Exception e) {
                                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                                    }
                                }
                            }
                        });
            }

            private void invalidemail(boolean b) {
                invalidEmail.setVisibility(View.VISIBLE);

                edtEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        invalidEmail.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            private void invalidPass(boolean toggle) {
                if (toggle) {
                    invalidPass1.setVisibility(View.VISIBLE);
                    invalidPass2.setVisibility(View.VISIBLE);
                }

                edtPass1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (edtPass1.getText().toString().length() > 6) {
                            invalidPass1.setVisibility(View.GONE);
                            if (edtPass2.getText().toString().length() > 6) {
                                invalidPass2.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

    }
}