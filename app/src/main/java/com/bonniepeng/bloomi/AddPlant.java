package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddPlant extends AppCompatActivity {

    private TextView txtTitle, txtSciName, txtNickname, txtHeight, txtNotes, edtNameEmpty, edtNicknameEmpty, txtNotifs;
    @SuppressLint("StaticFieldLeak")
    private static TextView txtTime, txtDate;
    private EditText edtName, edtNickname, edtHeight, edtNotes;
    private Spinner notifsFrequency, metric;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch swtchNotifs;
    private Button addToGarden, btnPickTime, btnPickDate;
    private ImageButton imageButton;
    private boolean notify, displayEmpty;
    private ScrollView parent;
    private FirebaseAuth mAuth;
    private static int year;
    private static int month;
    private static int day;
    private static int hour;
    private static int minute;

    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        instantiate();

        // SETTING NOTIFS
        swtchNotifs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!swtchNotifs.isChecked()) {
                    notify = false;
                    btnPickDate.setVisibility(View.GONE);
                    btnPickTime.setVisibility(View.GONE);
                    txtDate.setVisibility(View.GONE);
                    txtTime.setVisibility(View.GONE);
                    notifsFrequency.setVisibility(View.GONE);
                } else {
                    notify = true;
                    btnPickDate.setVisibility(View.VISIBLE);
                    btnPickTime.setVisibility(View.VISIBLE);
                    txtDate.setVisibility(View.VISIBLE);
                    txtTime.setVisibility(View.VISIBLE);
                    notifsFrequency.setVisibility(View.VISIBLE);
                }
            }
        });

        // SETTING IMAGE
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(AddPlant.this);
            }
        });

        // SUBMITTING
        addToGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNickname.getText().toString().equals("") && edtName.getText().toString().equals("")) {
                    Snackbar.make(parent, "Please give the plant at least 1 name.", Snackbar.LENGTH_LONG)
                            .show();
                    displayEmpty = true;
                    emptyField();
                } else if (notify && txtDate.getText().equals("dd / mm / yyyy")) {
                    Snackbar.make(parent, "Please set a notification date.", Snackbar.LENGTH_LONG)
                            .show();
                } else if (notify && txtTime.getText().equals("hh : mm")) {
                    Snackbar.make(parent, "Please set a notification time.", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    // ADD TO DATABASE
                    Map<String, Object> plant = new HashMap<>();
                    plant.put("userID", currentUser.getUid());
                    plant.put("sciName", edtName.getText().toString());
                    plant.put("plantID", getNewID());
                    plant.put("otherNotes", edtNotes.getText().toString());
                    plant.put("notif", notify);
                    if (notify) {
                        plant.put("notifYear", year);
                        plant.put("notifMonth", month);
                        plant.put("notifDay", day);
                        plant.put("notifHour", hour);
                        plant.put("notifMinute", minute);
                        plant.put("notifFrequency", notifsFrequency.getSelectedItem().toString());
                    }
                    plant.put("nickname", edtNickname.getText().toString());
                    // TODO: IMAGE AAAAh
                    plant.put("growthMeasurement", edtHeight.getText());
                    plant.put("growthMetric", metric.getSelectedItem().toString());
                    plant.put("growthDate",
                            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

                    db.collection("plants").add(plant)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("ADD PLANT", "DocumentSnapshot added with ID: " + documentReference.getId());

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("ADD PLANT", "Error adding document", e);
                                }
                            });


                    Snackbar.make(parent, "Added to Garden!", Snackbar.LENGTH_LONG)
                            .show();

                    finish();

                }
            }

            private int getNewID() {
                final int[] count = {0};

                db.collection("plants").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        count[0]++;
                                    }
                                } else {
                                    Log.d("GETDOCUMENTS", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                return count[0]++;
            }

            // EMPTY NAMES
            private void emptyField() {
                if (displayEmpty) {
                    edtNicknameEmpty.setVisibility(View.VISIBLE);
                    edtNameEmpty.setVisibility(View.VISIBLE);
                }

                edtNickname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!edtNickname.getText().toString().equals("")) {
                            edtNicknameEmpty.setVisibility(View.GONE);
                            edtNameEmpty.setVisibility(View.GONE);
                            displayEmpty = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                edtName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!edtName.getText().toString().equals("")) {
                            edtNicknameEmpty.setVisibility(View.GONE);
                            edtNameEmpty.setVisibility(View.GONE);
                            displayEmpty = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });

    }

    // SETTING PICTURE FOR PLANT

    // displaying options dialog
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take a Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add a Picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take a Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(takePictureIntent, 0);
                    } catch (ActivityNotFoundException e) {
                        dialog.dismiss();
                        Toast.makeText(AddPlant.this, "Camera is not accessible.", Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    try {
                        startActivityForResult(pickPhoto, 1);
                    } catch (ActivityNotFoundException e) {
                        dialog.dismiss();
                        Toast.makeText(AddPlant.this, "Gallery is not accessible.", Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // code for each option
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                // camera
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageButton.setImageBitmap(imageBitmap);
                    }
                    break;

                // gallery
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imageButton.setImageURI(selectedImage);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
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
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @SuppressLint("SetTextI18n")
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String ampm = "am";
            int newHour = hourOfDay;
            String newMinute = Integer.toString(minute);

            if (hourOfDay > 12) {
                newHour -= 12;
                ampm = "pm";
            } else if (hourOfDay == 12) {
                ampm = "pm";
            } else if (hourOfDay == 0) {
                newHour = 12;
            }

            if (minute < 10) {
                newMinute = "0" + minute;
            }

            txtTime.setText(newHour + " : " + newMinute + " " + ampm);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    // PICKING THE NOTIF DATE
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String newDay = Integer.toString(day);
            String newMonth = Integer.toString(month + 1);

            if (day < 10) {
                newDay = "0" + newDay;
            }
            if (month++ < 10) {
                newMonth = "0" + newMonth;
            }

            txtDate.setText(newDay + " / " + newMonth + " / " + year);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    //INSTANTIATING
    private void instantiate() {
        // TEXTVIEWS
        txtTitle = findViewById(R.id.txtTitle);
        txtSciName = findViewById(R.id.txtPlantType);
        txtNickname = findViewById(R.id.txtNickname);
        txtHeight = findViewById(R.id.txtHeight);
        txtNotes = findViewById(R.id.txtNotes);
        txtNotifs = findViewById(R.id.txtNotifs);
        txtTime = findViewById(R.id.txtTime);
        txtDate = findViewById(R.id.txtDate);
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

        // SWITCH
        swtchNotifs = findViewById(R.id.swtchNotifs);

        // BUTTONS
        addToGarden = findViewById(R.id.btnAddToGarden);
        imageButton = findViewById(R.id.imageButton);
        btnPickDate = findViewById(R.id.btnDate);
        btnPickTime = findViewById(R.id.btnTime);

        // BOOLEANS
        displayEmpty = false;

        // LAYOUTS
        parent = findViewById(R.id.parent);

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();

    }
}