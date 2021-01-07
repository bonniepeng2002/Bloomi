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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePassDialog extends Dialog {

    public Activity activity;
    public Context context;
    public Dialog dialog;
    private TextView badPass, txtWrongPass, txtWeakPass, txtPassNoMatch, wrongPass, weakPass, passNoMatch;
    private Button cancel, ok;
    private EditText edtOldPass, edtNewPass1, edtNewPass2;
    private FirebaseAuth mAuth;


    public ChangePassDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_pass_dialog);

        // INSTANTIATING
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        context = this.getContext();
        instantiate();


        // RETRIEVE PASSWORD
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userPass = preferences.getString("password", "");
        Log.i("PASSWORD", userPass);


        // OK
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser != null) {

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

                                        // update password
                                    }
                                });


                } else {
                    Snackbar.make(view, "Error, you are not logged in. Try logging out and in again, and report this bug in Settings.", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    })
                    .show();
                    Log.i("CHANGE PASSWORD", "Current user was null");
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

    private void instantiate() {
        // TEXTVIEWS
        txtWrongPass = findViewById(R.id.txtWrongPass);
        txtWeakPass = findViewById(R.id.txtWeakPass1);
        txtPassNoMatch = findViewById(R.id.txtPassNoMatch);
        wrongPass = findViewById(R.id.wrongOldPass);
        weakPass = findViewById(R.id.weakPass);
        passNoMatch = findViewById(R.id.passNoMatch);

        // BUTTONS
        ok = findViewById(R.id.RPOk);
        cancel = findViewById(R.id.RPCancel);

        // EDITTEXTS
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass1 = findViewById(R.id.edtNewPass1);
        edtNewPass2 = findViewById(R.id.edtNewPass2);

    }


    private void wrongPass() {

        wrongPass.setVisibility(View.VISIBLE);
        txtWrongPass.setVisibility(View.VISIBLE);

        edtOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtOldPass.getText().toString().equals("")) {
                    wrongPass.setVisibility(View.GONE);
                    txtWrongPass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}
