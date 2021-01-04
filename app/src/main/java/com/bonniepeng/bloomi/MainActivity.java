package com.bonniepeng.bloomi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bonniepeng.bloomi.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    //TODO: learn about and create database for your plants
    //TODO: make dashboard
    //TODO: make garden
    //TODO: make notifications for watering for the app
    //TODO: UI design
    //TODO: logo
    //TODO: loading screen??


    private FloatingActionButton addPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiate();

        //TABS
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        //ADDPLANT
        addPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPlant.class);
                startActivity(intent);
            }
        });
    }

    private void instantiate() {
        addPlant = findViewById(R.id.addPlant);
    }
}