package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.jjoe64.graphview.GraphView;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class PlantCardView extends AppCompatActivity {


    // TODO: make graph of growth from data
    // TODO: setText for plantFrequency, plantTime, plantMeasurement, plantNextNotif
    //  based on database info
    // TODO: implement "edit" activity

    private TextView plantName, plantType, plantWatering, plantFrequency, at,
            plantTime, plantNext, plantNextNotif, plantGrowth, plantLastMeasurement, plantMeasurement,
            plantOtherNotes, plantNotes;
    private Button btnEdit, btnBack, plantAddMeasurement;
    private CardView imageWrapper;
    private ImageView plantImage;
    private GraphView graph;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_card_view);

        instantiate();
        Objects.requireNonNull(getSupportActionBar()).hide();

        // FIREBASE
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // INTENT
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String plantID = intent.getStringExtra("plantID");
        String sciName = intent.getStringExtra("sciName");
        String nickname = intent.getStringExtra("nickname");
        String imgPath = intent.getStringExtra("imgPath");
        String metric = intent.getStringExtra("growthMetric");
        boolean notif = intent.getBooleanExtra("notif", false);
        String notes = intent.getStringExtra("otherNotes");
        String growthDate = intent.getStringExtra("growthDate");
        int notifDay = intent.getIntExtra("notifDay", -1);
        int notifMonth = intent.getIntExtra("notifMonth", -1);
        int notifYear = intent.getIntExtra("notifYear", -1);
        int notifHour = intent.getIntExtra("notifHour", -1);
        int notifMinute = intent.getIntExtra("notifMinute", -1);
        String notifFrequency = intent.getStringExtra("notifFrequency");
        Map<String, Double> measurements = (HashMap<String, Double>) bundle.get("growthMeasurement");
        TreeMap<String, Double> sortedMeasurements = new TreeMap<>(new DateComparator());
        for (Map.Entry<String, Double> entry : measurements.entrySet()) {
            sortedMeasurements.put(entry.getKey(), entry.getValue());
        }

        Log.i("EXTRAS", String.valueOf(bundle));
        Log.i("SORTED MEASUREMENTS", measurements.toString());


        // SETTING DATA
        if (!nickname.equals("") && nickname != null) {
            plantName.setText(nickname);
            plantType.setText(sciName);
        } else {
            plantName.setText(sciName);
            plantType.setText(nickname);
        }
        // Reference to an image file in Cloud Storage
        Picasso.get()
                .load(imgPath)
                .fit()
                .centerCrop()
                .into(plantImage);
        plantNotes.setText(notes);
        // set latest measurement to latest measurement in database
        String latestMeasurement = String.valueOf(sortedMeasurements.get(sortedMeasurements.lastKey()));
        Log.i("LATEST MEASUREMENT VALUE", latestMeasurement);
        plantMeasurement.setText(latestMeasurement + " " + metric);
        plantFrequency.setText(notifFrequency);
        if (notifFrequency.equals("")) {
            plantTime.setText("");
            plantNextNotif.setText("");
            // TODO: dont show "at" and "next up"
            // TODO: display "none"
        } else {
            // TODO: show all info
        }


        // GO BACK
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // ADD A MEASUREMENT
        plantAddMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddMeasureDialog cdd = new AddMeasureDialog(PlantCardView.this);
                InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                cdd.getWindow().setBackgroundDrawable(inset);
                cdd.show();

                Button ok = (Button) cdd.findViewById(R.id.btnOk);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String edtMeasurement = ((EditText) cdd.findViewById(R.id.edtNewMeasure)).getText().toString();
                        String newMetric = ((Spinner) cdd.findViewById(R.id.newMetric)).getSelectedItem().toString();
                        TextView txtEmpty = (TextView) cdd.findViewById(R.id.txtEmpty);

                        if (edtMeasurement.equals("")) {
                            txtEmpty.setVisibility(View.VISIBLE);
                        } else {
                            double newMeasurement =
                                    Double.parseDouble(new DecimalFormat("##.##").format
                                            (Double.valueOf(edtMeasurement)));

                            plantMeasurement.setText(newMeasurement + " " + newMetric);
                            // TODO: convert metric to first ever metric???
                            measurements.put((new LocalDate()).toString(), newMeasurement);

                            // update database
                            db.collection("users")
                                    .document(currentUser.getUid())
                                    .collection("plants")
                                    .document(plantID)
                                    .update("growthMeasurement", measurements);

                            cdd.dismiss();
                            Toast.makeText(PlantCardView.this, "Measurement added.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    // to sort measurement by date
    static class DateComparator implements Comparator<String>, Serializable {

        public int compare(String date1, String date2) {
            int date1Int = convertDateToInteger(date1);
            int date2Int = convertDateToInteger(date2);

            return date1Int - date2Int;
        }

        // converts date string with format YYYY-MM-DD to integer of value YYYYMMDD
        private int convertDateToInteger(String date) {
            String[] tokens = date.split("-");

            return Integer.parseInt(tokens[0] + tokens[1] + tokens[2]);
        }
    }

    private void instantiate() {

        // TEXTVIEWS
        plantName = findViewById(R.id.plantName);
        plantType = findViewById(R.id.plantType);
        plantWatering = findViewById(R.id.plantWatering);
        plantFrequency = findViewById(R.id.plantFrequency);
        at = findViewById(R.id.at);
        plantTime = findViewById(R.id.plantTime);
        plantNext = findViewById(R.id.plantNext);
        plantNextNotif = findViewById(R.id.plantNextNotif);
        plantGrowth = findViewById(R.id.plantGrowth);
        plantLastMeasurement = findViewById(R.id.plantLastMeasurement);
        plantMeasurement = findViewById(R.id.plantMeasurement);
        plantOtherNotes = findViewById(R.id.plantOtherNotes);
        plantNotes = findViewById(R.id.plantNotes);

        // BUTTONS
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        plantAddMeasurement = findViewById(R.id.plantAddMeasurement);

        // CARDVIEWS
        imageWrapper = findViewById(R.id.imageWrapper);

        // IMAGEVIEWS
        plantImage = findViewById(R.id.plantImage);

        // GRAPHVIEWS
        graph = findViewById(R.id.graph);

    }

}