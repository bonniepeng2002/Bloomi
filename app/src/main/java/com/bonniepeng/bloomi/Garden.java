package com.bonniepeng.bloomi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Garden#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Garden extends Fragment implements RecyclerOnItemClick {

    // STARTING CODE - not used.
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private final String TAG = "GARDEN INFO";

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

    // --------------------------------------------------------------------

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    // user's list of plants
    private ArrayList<Map<String, Object>> plants = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garden, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        plants.clear();

        // garden list of plants UI
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), this);
        RecyclerView gardenRecycler = requireView().findViewById(R.id.gardenRecycler);
        gardenRecycler.setAdapter(adapter);
        gardenRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // sort measurements by date
        Query sortQuery = dbRef.child("users").child(currentUser.getUid())
                .child("plants").child("growthMeasurement").orderByKey();
        sortQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // getting user collection of plants
        CollectionReference userPlants = db.collection("users")
                .document(currentUser.getUid()).collection("plants");

        // for every document, get its fields as a Map, and add plantID
        // adds each document to plants
        userPlants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Map<String, Object> plant = document.getData();

                        // do not add initial plant
                        if (!document.getId().equals("initial plant")) {
                            plant.put("plantID", document.getId());
                            plants.add(plant); // adds Map<Key, Value> along with <plantID, id>
                        }
                    }
                    Log.i(TAG, plants.toString());
                    adapter.setPlants(plants);

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    // ON EACH PLANT ITEM CLICK
    @Override
    public void onClick(View view, int position) {
        Map<String, Object> currentPlant = plants.get(position);
        Intent intent = new Intent(getActivity(), PlantCardView.class);

        // pass all plant data to PlantCardView
        for (Map.Entry<String, Object> entry : currentPlant.entrySet()) {
            intent.putExtra(entry.getKey(), (Serializable) entry.getValue());
            Log.i("CURRENT INTENT TYPE", entry.getValue().getClass().toGenericString());
        }

        // for debug purposes
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.i("PLANT CLICK INTENT", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
            }
        }

        startActivity(intent);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


}