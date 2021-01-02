package com.bonniepeng.bloomi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Garden#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Garden extends Fragment {

    private int id = 0;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Garden() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Garden.
     */
    public static Garden newInstance(String param1, String param2) {
        Garden fragment = new Garden();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // BLOOMI'S OBJECTS

    // METHODS

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garden, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity());
        RecyclerView gardenRecycler = Objects.requireNonNull(getView()).findViewById(R.id.gardenRecycler);
        gardenRecycler.setAdapter(adapter);
        gardenRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        ArrayList<Plant> plants = new ArrayList<>();
        while (id<8){
            id +=1;
            plants.add(new Plant("a", "b", "c", "d", "e", "f", "g", id));
        }

        adapter.setPlants(plants);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        instantiate();

        //  TODO: on card click, call onPlantClick()

    }

    private void instantiate() {




    }


}