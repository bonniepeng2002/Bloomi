package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

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
    private String Document_img1="";


    //TODO: check for all required fields and proper data when clicking add to garden

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

        // SETTING IMAGE - opening the gallery
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();

//                startActivityForResult(new Intent
//                                (Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
//                        GET_FROM_GALLERY);
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
                    //TODO: change this for when we use databases
                    Snackbar.make(parent, "SUCCESS", Snackbar.LENGTH_LONG)
                            .show();
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

    //TODO: allow user to access the camera to take a picture.
    // right now only the gallery option works.
    //TODO: make sure the images from camera roll fit into image button nicely.

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlant.this);
        builder.setTitle("Upload a picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    imageButton.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                //TODO: save this image inside database so we can access it easily later
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imageButton.setImageURI(selectedImage);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                        imageButton.setImageBitmap(selectedImage);
//                    }
//
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }
//
//                    }
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_image_capture, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


//    // ONCE USER SELECTS IMAGE FROM GALLERY
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //Detects request codes
//        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = data.getData();
//            //TODO: save this image inside database so we can access it easily later
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                imageButton.setImageURI(selectedImage);
//
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }

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
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int newMonth = month;
            newMonth += 1;
            txtDate.setText(day + " / " + newMonth + " / " + year);
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
    }
}