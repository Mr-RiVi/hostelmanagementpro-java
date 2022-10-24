package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hostelmanagementpro.model.FloorModel;

import java.util.ArrayList;
import java.util.List;

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorHolder> {
    private List<FloorModel> floors = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onHolderClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    @NonNull
    @Override
    public FloorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_floor, parent, false);
        return new FloorHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FloorAdapter.FloorHolder holder, int position) {
        FloorModel currentFloor = floors.get(position);
        holder.view_number.setText(currentFloor.getNumber());
        holder.view_roomCount.setText(currentFloor.getRoomCount());
    }

    @Override
    public int getItemCount() {
        return floors.size();
    }

    public void setFloors(List<FloorModel> floors) {
        this.floors = floors;
        notifyDataSetChanged();
    }

    class FloorHolder extends RecyclerView.ViewHolder {
        private TextView view_number;
        private TextView view_roomCount;

        public FloorHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            view_number = itemView.findViewById(R.id.I_floor_no);
            view_roomCount = itemView.findViewById(R.id.I_room_count);

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