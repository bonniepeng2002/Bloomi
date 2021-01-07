package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddMeasureDialog extends Dialog {

    public Activity activity;
    public Dialog dialog;
    public Button btnOk, btnCancel;
    private TextView txtEmpty;
    private Spinner metric;
    private EditText newMeasurement;
    private FirebaseAuth mAuth;

    public AddMeasureDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_measurement_dialog);

        // INSTANTIATING
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        metric = findViewById(R.id.newMetric);
        newMeasurement = findViewById(R.id.edtNewEmail);
        txtEmpty = findViewById(R.id.txtEmpty);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // OK
        btnOk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (newMeasurement.getText().toString().equals("")) {
                    txtEmpty.setVisibility(View.VISIBLE);
                } else {
                    // TODO: add new data to database
                    // TODO: make sure the data changes in PlantCardView.java too
                    Toast.makeText(activity, "success for now", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        // CANCEL
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
