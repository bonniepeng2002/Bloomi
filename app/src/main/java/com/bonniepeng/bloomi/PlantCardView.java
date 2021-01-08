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

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;

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
        Map<String, Float> measurements = (Map<String, Float>) bundle.get("growthMeasurement");
        Log.i("GETTING EXTRAS", String.valueOf(bundle));


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
        // find latest measurement by date and time
        String latestMeasurement = "";
        Log.i("LATEST MEASUREMENT VALUE", latestMeasurement);
//        plantMeasurement.setText(latestMeasurement);


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
//                Bundle args = new Bundle();
//                args.putString("ID", plantID);

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
                            float newMeasurement =
                                    Float.parseFloat(new DecimalFormat("##.##").format
                                            (Float.valueOf(edtMeasurement)));

                            plantMeasurement.setText(newMeasurement + " " + newMetric);
                            // TODO: convert metric to first ever metric???
                            measurements.put(Timestamp.now().toDate().toString(), newMeasurement);

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

//                  this would be easiest way to exchange data between dialog and activity. But it is ugly.
//                AlertDialog.Builder cdd = new AlertDialog.Builder(PlantCardView.this)
//                        .setView(R.layout.add_measurement_dialog);
//                cdd.show();
            }
        });

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