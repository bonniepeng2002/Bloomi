package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.jjoe64.graphview.GraphView;

public class PlantCardView extends AppCompatActivity {

    // TODO: start database tutorial!!!


    // TODO: make graph of growth from data
    // TODO: setText for plantFrequency, plantTime, plantMeasurement, plantNextNotif, plantNotes, plantName, plantType, and plantImage
    //  based on database info
    // TODO: implement "edit" activity
    // TODO: add onclick for plant add measurement

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

        int plantId = getIntent().getIntExtra("PLANT_ID", 0);

        plantType.setText(Integer.toString(plantId));

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
                CustomDialog cdd = new CustomDialog(PlantCardView.this);
                InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                cdd.getWindow().setBackgroundDrawable(inset);
                cdd.show();
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