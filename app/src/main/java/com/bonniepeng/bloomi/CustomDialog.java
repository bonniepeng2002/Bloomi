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

import com.google.android.material.snackbar.Snackbar;

public class CustomDialog extends Dialog {

    public Activity activity;
    public Dialog dialog;
    public Button btnOk, btnCancel;
    private TextView plantMeasurement, txtEmpty;
    private Spinner metric;
    private EditText newMeasurement;

    public CustomDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_measurement_dialog);

        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        plantMeasurement = findViewById(R.id.plantMeasurement);
        metric = findViewById(R.id.newMetric);
        newMeasurement = findViewById(R.id.edtNewMeasurement);
        txtEmpty = findViewById(R.id.txtEmpty);

        // OK
        btnOk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (newMeasurement.getText().toString().equals("")) {
                    txtEmpty.setVisibility(View.VISIBLE);
                } else {
                    // TODO: add new data to database
                    plantMeasurement.setText(newMeasurement.getText().toString() + " " + metric.getSelectedItem().toString());
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
