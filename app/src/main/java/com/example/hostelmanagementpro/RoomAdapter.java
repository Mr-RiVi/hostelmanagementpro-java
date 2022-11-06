package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hostelmanagementpro.model.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomHolder> {
    private List<RoomModel> rooms = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onHolderClick(int position);
        void onUpdateClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
        RoomModel currentRoom = rooms.get(position);
        holder.view_room_no.setText(currentRoom.getRoomNo());
        holder.view_room_type.setText(currentRoom.getRoomType());
        holder.view_room_status.setText(currentRoom.getRoomStatus());
        holder.view_student_count.setText(currentRoom.getStuCount());
        holder.view_bed_count.setText(currentRoom.getBedCount());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void setRooms(List<RoomModel> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }

    class RoomHolder extends RecyclerView.ViewHolder {
        private TextView view_room_no;
        private TextView view_room_type;
        private TextView view_room_status;
        private TextView view_student_count;
        private TextView view_bed_count;
        private Button btn_update_room;


        public RoomHolder(View itemView, final RoomAdapter.OnItemClickListener listener) {
            super(itemView);
            view_room_no = itemView.findViewById(R.id.I_room_N0);
            view_room_type = itemView.findViewById(R.id.I_room_Type);
            view_room_status = itemView.findViewById(R.id.I_room_Status);
            view_student_count = itemView.findViewById(R.id.I_student_Count);
            view_bed_count = itemView.findViewById(R.id.I_bed_Count);
            btn_update_room = itemView.findViewById(R.id.btn_update_room);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onHolderClick(position);
                    }
                }
            });

            btn_update_room.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUpdateClick(position);
                    }
                }
            });
        }
    }


}