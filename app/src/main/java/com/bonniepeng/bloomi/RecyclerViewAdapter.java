package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ArrayList<Map<String, Object>> plants = new ArrayList<>();
    private Context context;
    private final String TAG = "ADAPTER INFLATE";

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public RecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.garden_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // TODO: set name and image
        Map<String, Object> currentPlant = plants.get(i);
        Log.i(TAG, currentPlant.toString());

        viewHolder.sciName.setText(currentPlant.get("sciName").toString()); // get value with key "sciName"
        viewHolder.txtType.setText(currentPlant.get("nickname").toString());

        // ON CARD CLICK
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlantCardView.class);
                // TODO: change based on db info
                intent.putExtra("PLANT_ID", currentPlant.get("plantID").toString());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setPlants(ArrayList<Map<String, Object>> plants) {
        this.plants = plants;
        notifyDataSetChanged();
        Log.i("IN SET PLANTS", plants.toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView sciName;
        private final TextView txtType;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            sciName = (TextView) itemView.findViewById(R.id.plantName);
            txtType = itemView.findViewById(R.id.txtType);
        }

    }
}
