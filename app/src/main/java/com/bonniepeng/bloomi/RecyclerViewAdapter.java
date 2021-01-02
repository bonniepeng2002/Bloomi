package com.bonniepeng.bloomi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Plant> plants = new ArrayList<>();
    private Context context;

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
        viewHolder.sciName.setText(plants.get(i).getSciName());
        viewHolder.txtType.setText(Integer.toString(plants.get(i).getId()));
        // set image here when we have database

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlantCardView.class);
                intent.putExtra("PLANT_ID", plants.get(i).getId());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView sciName, txtType;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            sciName = (TextView) itemView.findViewById(R.id.plantName);
            txtType = itemView.findViewById(R.id.txtType);
        }

    }
}
