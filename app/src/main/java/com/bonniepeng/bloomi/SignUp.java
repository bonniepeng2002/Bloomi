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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText edtEmail, edtPass1, edtPass2;
    private Button signUp, cancel;
    private TextView invalidEmail, invalidPass1, invalidPass2, txtBadEmail, txtBadPass;
    private FirebaseAuth mAuth;
    private final String TAG = "SIGNUP";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        instantiate();

        // CANCEL
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // SIGNING UP
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getting input
                String email = edtEmail.getText().toString();
                String pass1 = edtPass1.getText().toString();
                String pass2 = edtPass2.getText().toString();

                invalidPass1.setVisibility(View.GONE);
                invalidPass2.setVisibility(View.GONE);
                invalidEmail.setVisibility(View.GONE);
                txtBadPass.setVisibility(View.GONE);
                txtBadEmail.setVisibility(View.GONE);

                // creating user
                if (!email.equals("")) {
                    if (pass1.equals(pass2)) {
                        if (!pass1.equals("")) {

                            // from Firebase's Docs
                            mAuth.createUserWithEmailAndPassword(email, pass1)
                                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign up success
                                                signUp.setEnabled(false);
                                                Log.i(TAG, "createUserWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                // adding user as an empty document to users
                                                db.collection("users").document(user.getUid())
                                                        .set(new HashMap<>())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.i(TAG, "user document added");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG, "user document COULD NOT be added");
                                                            }
                                                        });

                                                // adding plants subcollection to user document
                                                db.collection("users").document(user.getUid())
                                                        .collection("plants")
                                                        .document("initial plant").set(new HashMap<>())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.i(TAG, "plants subcollection added");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG, "plants subcollection COULD NOT be added");
                                                            }
                                                        });

                                                // Go to Home
                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                startActivity(intent);

                                            } else {
                                                try {
                                                    throw Objects.requireNonNull(task.getException());

                                                } catch (FirebaseAuthWeakPasswordException e) {
                                                    invalidPass("Password must have at least 6 characters.");

                                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                                    invalidEmail("Please enter a valid email.");

                                                } catch (FirebaseAuthUserCollisionException e) {
                                                    invalidEmail("User has already registered.");

                                                } catch (Exception e) {
                                                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                                                }
                                            }
                                        }
                                    });
                        } else {
                            invalidPass("Password cannot be blank.");
                        }
                    } else {
                        invalidPass("Passwords must match.");
                    }
                } else {
                    invalidEmail("Email cannot be blank.");
                }
            }


            private void invalidEmail(String message) {

                invalidEmail.setVisibility(View.VISIBLE);
                txtBadEmail.setText(message);
                txtBadEmail.setVisibility(View.VISIBLE);

                edtEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        invalidEmail.setVisibility(View.GONE);
                        txtBadEmail.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            private void invalidPass(String message) {

                invalidPass1.setVisibility(View.VISIBLE);
                invalidPass2.setVisibility(View.VISIBLE);
                txtBadPass.setText(message);
                txtBadPass.setVisibility(View.VISIBLE);


                edtPass1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (edtPass1.getText().toString().length() > 6) {
                            invalidPass1.setVisibility(View.GONE);
                            txtBadPass.setVisibility(View.GONE);
                            if (edtPass2.getText().toString().length() > 6) {
                                invalidPass2.setVisibility(View.GONE);
                            }
                        }
                        if (!edtPass1.getText().toString().equals(edtPass2.getText().toString())) {
                            invalidPass("Passwords must match.");
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
        cancel = findViewById(R.id.btnSUCancel);

        // TEXTVIEWS
        invalidEmail = findViewById(R.id.emailInvalid);
        invalidPass1 = findViewById(R.id.passInvalid1);
        invalidPass2 = findViewById(R.id.passInvalid2);
        txtBadEmail = findViewById(R.id.txtSUBadEmail);
        txtBadPass = findViewById(R.id.txtSUBadPass);

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

    }
}