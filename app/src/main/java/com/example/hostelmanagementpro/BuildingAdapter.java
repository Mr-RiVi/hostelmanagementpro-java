package com.example.hostelmanagementpro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostelmanagementpro.model.BuildingModel;

import java.util.ArrayList;
import java.util.List;


public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingHolder> {
        private List<BuildingModel> buildings = new ArrayList<>();
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onHolderClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        @NonNull
        @Override
        public BuildingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_building, parent, false);
            return new BuildingHolder(itemView,mListener);
        }

        @Override
        public void onBindViewHolder(@NonNull BuildingHolder holder, int position) {
            BuildingModel currentBuilding = buildings.get(position);
            holder.view_number.setText(currentBuilding.getNumber());
            holder.view_name.setText(currentBuilding.getName());
            holder.view_gender.setText(currentBuilding.getGender());
        }

        @Override
        public int getItemCount() {
            return buildings.size();
        }

        public void setBuildings(List<BuildingModel> buildings) {
            this.buildings = buildings;
            notifyDataSetChanged();
        }

        class BuildingHolder extends RecyclerView.ViewHolder {
            private TextView view_number;
            private TextView view_name;
            private TextView view_gender;

            public BuildingHolder(View itemView, final OnItemClickListener listener) {
                super(itemView);
                view_number = itemView.findViewById(R.id.I_building_No);
                view_name = itemView.findViewById(R.id.I_building_Name);
                view_gender = itemView.findViewById(R.id.I_gender);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onHolderClick(position);
                        }
                    }
                });
            }
        }
    }