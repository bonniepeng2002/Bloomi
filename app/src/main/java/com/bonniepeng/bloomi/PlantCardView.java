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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class PlantCardView extends AppCompatActivity {


    // TODO: make graph of growth from data
    // TODO: setText for plantFrequency, plantTime, plantMeasurement, plantNextNotif
    //  based on database info
    // TODO: implement "edit" activity

    private TextView plantName, plantType, plantWatering, plantFrequency, at,
            plantTime, plantNext, plantNextNotif, plantGrowth, plantMeasurement, plantNotes;
    private Button btnEdit, btnBack, plantAddMeasurement;
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
        Map<String, Long> measurements = (HashMap<String, Long>) bundle.get("growthMeasurement");

        SortedMap<String, Long> sortedMeasurements = new TreeMap<>(new DateComparator());
        for (Map.Entry<String, Long> entry : measurements.entrySet()) {
            sortedMeasurements.put(entry.getKey(), entry.getValue());
        }

        Log.i("EXTRAS", String.valueOf(bundle));
        Log.i("SORTED MEASUREMENTS", sortedMeasurements.toString());


        // SETTING DATA
        if (!nickname.equals("")) {
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
            plantTime.setText(notifHour + " : " + notifMinute);
        }

        // graph

        ArrayList<DataPoint> measurementsData = new ArrayList<>();
        Iterator<Map.Entry<String, Long>> it = sortedMeasurements.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> pair = it.next();
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(pair.getKey());
                measurementsData.add(new DataPoint(date, pair.getValue()));
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error generating graph.", Toast.LENGTH_SHORT).show();
            }
        }

        Log.i("MEASUREMENTS DATA", measurementsData.toString());
        Log.i("PASSING INTO GRAPH", Arrays.toString(measurementsData.toArray(new DataPoint[0])));

        LineGraphSeries<DataPoint> series =
                new LineGraphSeries<DataPoint>
                        (measurementsData.toArray(new DataPoint[0]));

        series.setColor(R.color.purple_700);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Height (" + metric + ")");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        // set manual x bounds to have nice steps

//        graph.getViewport().setMinX(measurementsData.get(0).getX()); // earliest date
//        graph.getViewport().setMaxX(measurementsData.get(measurementsData.size() - 1).getX(); // latest date
//        double minY = measurementsData.get(0).getY()/5;
//        double maxY = measurementsData.get(0).getY()*5;
//        graph.getViewport().setMinY(minY);
//        graph.getViewport().setMaxY(maxY);
//        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.addSeries(series);


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
                            long newMeasurement =
                                    Long.parseLong(new DecimalFormat("##.##").format
                                            (Long.valueOf(edtMeasurement)));

                            plantMeasurement.setText(newMeasurement + " " + newMetric);
                            // TODO: convert metric to first ever metric???
                            measurements.put((new LocalDate()).toString(), newMeasurement);

                            // update database
                            assert currentUser != null;
                            assert plantID != null;
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
        plantMeasurement = findViewById(R.id.plantMeasurement);
        plantNotes = findViewById(R.id.plantNotes);

        // BUTTONS
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        plantAddMeasurement = findViewById(R.id.plantAddMeasurement);

        // IMAGEVIEWS
        plantImage = findViewById(R.id.plantImage);

        // GRAPHVIEWS
        graph = findViewById(R.id.graph);

    }

}