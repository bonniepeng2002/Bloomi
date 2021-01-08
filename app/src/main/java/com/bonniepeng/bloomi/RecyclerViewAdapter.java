package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    private ArrayList<Map<String, Object>> plants = new ArrayList<>();
    private Context context;
    private final String TAG = "ADAPTER INFLATE";
    private RecyclerOnItemClick recyclerOnItemClick;


    public RecyclerViewAdapter(Context context, RecyclerOnItemClick recyclerOnItemClick) {
        this.context = context;
        this.recyclerOnItemClick = recyclerOnItemClick;
    }

    public RecyclerViewAdapter() {
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.garden_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, recyclerOnItemClick);

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Map<String, Object> currentPlant = plants.get(i);
        Log.i(TAG, currentPlant.toString());

        // SETTING DISPLAY INFO
        String sciName = currentPlant.get("sciName").toString(); // get value with key "sciName"
        String nickname = currentPlant.get("nickname").toString();
        String imgPath = currentPlant.get("imgPath").toString();


        // if type is empty, set big name to nickname
        // if both names exist, nickname takes precedence
        if (sciName.equals("") || !nickname.equals("")) {
            viewHolder.sciName.setText(nickname);
            viewHolder.txtType.setText(sciName);
        } else {
            viewHolder.sciName.setText(sciName);
            viewHolder.txtType.setText(nickname);
        }

        // Reference to an image file in Cloud Storage
        Picasso.get()
                .load(imgPath)
                .fit()
                .centerCrop()
                .into(viewHolder.img);


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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView sciName;
        private final TextView txtType;
        private final CardView parent;
        private final ImageView img;
        private RecyclerView recycler;
        RecyclerOnItemClick recyclerOnItemClick;


        public ViewHolder(@NonNull View itemView, RecyclerOnItemClick recyclerOnItemClick) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            sciName = (TextView) itemView.findViewById(R.id.plantName);
            txtType = itemView.findViewById(R.id.txtType);
            img = itemView.findViewById(R.id.plantImage);
            recycler = itemView.findViewById(R.id.gardenRecycler);

            this.recyclerOnItemClick = recyclerOnItemClick;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerOnItemClick.onClick(view, getAdapterPosition());
        }
    }
}
