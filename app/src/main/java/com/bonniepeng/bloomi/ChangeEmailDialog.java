package com.bonniepeng.bloomi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangeEmailDialog extends Dialog {

    public Activity activity;
    public Context context;
    public Dialog dialog;
    private TextView badEmail, txtBadEmail;
    private Button cancel, ok;
    private EditText newEmail;
    private FirebaseAuth mAuth;


    public ChangeEmailDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_email_dialog);

        // INSTANTIATING
        badEmail = findViewById(R.id.badEmail);
        txtBadEmail = findViewById(R.id.txtBadEmail);
        cancel = findViewById(R.id.CECancel);
        ok = findViewById(R.id.CEOk);
        newEmail = findViewById(R.id.edtNewEmail);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        context = this.getContext();


        // RETRIEVE PASSWORD
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userPass = preferences.getString("password", "");
        Log.i("PASSWORD", userPass);


        // OK
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = newEmail.getText().toString();

                if (currentUser != null) {
                    if (currentUser.getEmail().equals(email)) {
                        Log.i("CHANGE EMAIL", "user is null");
                        badEmail("New email cannot be current email.");

                    } else {

                        // Get auth credentials from the user for re-authentication
                        // TODO: change this password from being hardcoded to userPass once you log out
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(currentUser.getEmail(), "bonniebob"); // Current Login Credentials
                        Log.i("CHANGE EMAIL", "finished getting credential");

                        // Prompt the user to re-provide their sign-in credentials
                        currentUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.i("REAUTHENTICATE USER", "User re-authenticated.");

                                        // changing email address
                                        currentUser.updateEmail(email)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {
                                                            Log.i("CHANGE EMAIL", "User email address updated.");
                                                            ((Globals) context.getApplicationContext()).setRefreshSettings(true);
                                                            Log.i("GLOBAL inside change email", Boolean.toString(((Globals) context.getApplicationContext()).isRefreshSettings()));

                                                            Snackbar.make(view, "Email successfully changed.", Snackbar.LENGTH_LONG)
                                                                    .show();
                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                public void run() {
                                                                    dismiss();
                                                                }
                                                            }, 1000);
                                                        } else {
                                                            try {
                                                                throw Objects.requireNonNull(task.getException());

                                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                                badEmail("Please enter a valid email.");

                                                            } catch (FirebaseAuthUserCollisionException e) {
                                                                badEmail("Email is already in use.");

                                                            } catch (Exception e) {
                                                                Log.e("CHANGE EMAIL", Objects.requireNonNull(e.getMessage()));
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }

                } else {
                    badEmail("Error, you are not logged in. \nTry logging out and in again, and \nreport this bug in Settings.");
                    Log.i("CHANGE EMAIL", "Current user was null");
                }
            }
        });

        // CANCEL
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    private void badEmail(String message) {

        badEmail.setVisibility(View.VISIBLE);
        txtBadEmail.setText(message);
        txtBadEmail.setVisibility(View.VISIBLE);

        newEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!newEmail.getText().toString().equals("") &&
                        android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail.getText().toString()).matches()) {
                    badEmail.setVisibility(View.GONE);
                    txtBadEmail.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}
