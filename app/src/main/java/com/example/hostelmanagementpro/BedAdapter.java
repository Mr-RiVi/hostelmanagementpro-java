package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hostelmanagementpro.model.BedModel;
import com.example.hostelmanagementpro.model.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class BedAdapter extends RecyclerView.Adapter<BedAdapter.BedHolder> {
    private List<BedModel> beds = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onHolderClick(int position);
        void onUpdateBedClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    @NonNull
    @Override
    public BedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bed, parent, false);
        return new BedHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BedHolder holder, int position) {
        BedModel currentBed = beds.get(position);
        holder.view_bed_no.setText(currentBed.getBedNo());
        holder.view_student_id.setText(currentBed.getStuId());
        holder.view_room_no.setText(currentBed.getRoomNo());
    }

    @Override
    public int getItemCount() {
        return beds.size();
    }

    public void setBeds(List<BedModel> beds) {
        this.beds = beds;
        notifyDataSetChanged();
    }

    class BedHolder extends RecyclerView.ViewHolder {
        private TextView view_bed_no;
        private TextView view_student_id;
        private TextView view_room_no;
        private Button view_update_btn;

        public BedHolder(View itemView, final BedAdapter.OnItemClickListener listener) {
            super(itemView);
            view_bed_no = itemView.findViewById(R.id.I_bed_No);
            view_student_id = itemView.findViewById(R.id.I_bed_student_Id);
            view_room_no = itemView.findViewById(R.id.I_Bed_room_No);
            view_update_btn = itemView.findViewById(R.id.I_bed_updateBtn);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onHolderClick(position);
                    }
                }
            });

            view_update_btn.setOnClickListener (v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUpdateBedClick(position);
                    }
                }
            });

        }
    }


}