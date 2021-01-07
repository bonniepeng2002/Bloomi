package com.bonniepeng.bloomi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassDialog extends Dialog {

    public Activity activity;
    public Context context;
    public Dialog dialog;
    private Button ok, cancel;
    private TextView resendBadEmail, txtResendBadEmail;
    private EditText edtResendEmail;
    private FirebaseAuth mAuth;


    public ForgotPassDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reset_pass_dialog);

        // INSTANTIATING

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        context = this.getContext();
        ok = findViewById(R.id.REOk);
        cancel = findViewById(R.id.RECancel);
        resendBadEmail = findViewById(R.id.resendBadEmail);
        txtResendBadEmail = findViewById(R.id.txtResendBadEmail);
        edtResendEmail = findViewById(R.id.edtResendEmail);


        // OK
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtResendEmail.getText().toString();

                if (!email.equals("")) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        txtResendBadEmail.setText("Verification email sent!");
                                        // TODO: change this below color to new purple_700 if you change it
                                        txtResendBadEmail.setTextColor(Color.parseColor("#048509"));
                                        txtResendBadEmail.setVisibility(View.VISIBLE);

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                txtResendBadEmail.setVisibility(View.GONE);
                                                txtResendBadEmail.setTextColor(Color.parseColor("#F44336"));
                                                dismiss();
                                            }
                                        }, 1500);
                                    } else {
                                        badEmail("Please enter the email associated with the account's password reset.");
                                    }
                                }
                            });
                } else {
                    badEmail("Email cannot be blank");
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

        resendBadEmail.setVisibility(View.VISIBLE);
        txtResendBadEmail.setText(message);
        txtResendBadEmail.setVisibility(View.VISIBLE);

        edtResendEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                resendBadEmail.setVisibility(View.GONE);
                txtResendBadEmail.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}
