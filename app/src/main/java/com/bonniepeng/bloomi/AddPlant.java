package com.bonniepeng.bloomi;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddPlant extends AppCompatActivity {

    private TextView txtTitle,txtSciName,txtNickname,txtHeight,txtNotes,edtNameEmpty,edtNicknameEmpty;
    private EditText edtName,edtNickname,edtHeight,edtNotes;
    private Spinner notifsFrequency,metric;
    private CheckBox cbNotifs;
    private Button addToGarden,pickTime;
    private ImageButton imageButton;
    private static int notifHour, notifMinute;
    private boolean notify, displayEmpty;

    //TODO: add frequency options
    //TODO: set time differently: allow time to be seen and editted after they set it for a first time
    // allow time to be seen after they set it
    // allow toggle between having a time or not
    //TODO: check for all required fields and proper data when clicking add to garden
    //TODO: allow to change image when image button is clicked


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        instantiate();

        // SETTING NOTIFS
        cbNotifs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!cbNotifs.isChecked()){
                    notify = false;
                    pickTime.setVisibility(View.GONE);
                    notifsFrequency.setVisibility(View.GONE);
                } else {
                    notify = true;
                    pickTime.setVisibility(View.VISIBLE);
                    notifsFrequency.setVisibility(View.VISIBLE);
                }
            }
        });

        // SUBMITTING
        addToGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNickname.getText().toString().equals("") && edtName.getText().toString().equals("")) {
                    Toast.makeText(AddPlant.this, "Please give the plant at least one type of name.", Toast.LENGTH_LONG).show();
                    displayEmpty = true;
                } else {
                    //TODO: change this for when we use databases
//                    Plant newPlant = new Plant(
//                            edtName.getText().toString(),
//                            edtNickname.getText().toString(),
//                            metric.getSelectedItem().toString(),
//                            edtHeight.getText().toString(),
//                            edtNotes.getText().toString(),
//                            notifsFrequency.getSelectedItem().toString(),
//                            Integer.toString(notifHour)+":"+Integer.toString(notifMinute),
//                            imageButton.
//                    )
                }
            }
        });

        // PLANT IMAGE



        // EMPTY NAMES
        if (displayEmpty){
            if (!edtNickname.getText().toString().equals("") || !edtName.getText().toString().equals("")){
                edtNicknameEmpty.setVisibility(View.GONE);
                edtNameEmpty.setVisibility(View.GONE);
            } else {
                edtNicknameEmpty.setVisibility(View.VISIBLE);
                edtNameEmpty.setVisibility(View.VISIBLE);
            }
        }



    }

    // LETTING USER PICK THE NOTIFICATION TIME
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            notifHour = hourOfDay;
            notifMinute = minute;
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void instantiate() {
        // TEXTVIEWS
        txtTitle = findViewById(R.id.txtTitle);
        txtSciName = findViewById(R.id.txtPlantType);
        txtNickname = findViewById(R.id.txtNickname);
        txtHeight = findViewById(R.id.txtHeight);
        txtNotes = findViewById(R.id.txtNotes);
        edtNameEmpty = findViewById(R.id.edtNameEmpty);
        edtNicknameEmpty = findViewById(R.id.edtNicknameEmpty);

        // EDIT TEXTS
        edtName = findViewById(R.id.edtPlantType);
        edtNickname = findViewById(R.id.edtNickname);
        edtHeight = findViewById(R.id.edtHeight);
        edtNotes = findViewById(R.id.edtNotes);

        // SPINNERS
        notifsFrequency = findViewById(R.id.notifsFrequency);
        metric = findViewById(R.id.metric);

        // CHECKBOX
        cbNotifs = findViewById(R.id.cbNotifs);

        // BUTTONS
        addToGarden = findViewById(R.id.btnAddToGarden);
        imageButton = findViewById(R.id.imageButton);
        pickTime = findViewById(R.id.btnTime);

        //BOOLEANS
        displayEmpty = false;
    }
}