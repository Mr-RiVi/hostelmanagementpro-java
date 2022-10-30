package com.example.hostelmanagementpro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostelmanagementpro.model.StuProfiles;

import java.util.ArrayList;

public class StudentProfilesFetchingAdapter extends RecyclerView.Adapter<StudentProfilesFetchingAdapter.ViewHolder> {

    ArrayList<StuProfiles> list;
    Context context;
    private ItemClickListener itemClickListener;

    public void setFilteredList(ArrayList<StuProfiles> filteredList){
        this.list=filteredList;
        notifyDataSetChanged();
    }
    public StudentProfilesFetchingAdapter(ArrayList<StuProfiles> list, Context context,ItemClickListener itemClickListener) {
        this.list = list;
        this.context = context;
        this.itemClickListener=itemClickListener;
        System.out.println("this is adapter class and the list is:"+list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stu_profiles,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StuProfiles stu=list.get(position);

        holder.stuID.setText(stu.getStuID());
        holder.stuName.setText(stu.getStuName());
        holder.stuMobile.setText(stu.getStuMobile());
        holder.stuEmgMobile.setText(stu.getStuEmgMobile());

        holder.roomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemRoomInfoClicked(list.get(position));
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemEditClicked(list.get(position));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemDeleteClicked(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener{
        void onItemRoomInfoClicked(StuProfiles profiles);
        void onItemEditClicked(StuProfiles profiles);
        void onItemDeleteClicked(StuProfiles profiles);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView stuID;
        TextView stuName;
        TextView stuMobile;
        TextView stuEmgMobile;

        ImageButton delete;
        ImageButton edit;
        ImageView roomInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stuID=itemView.findViewById(R.id.stu_id);
            stuName=itemView.findViewById(R.id.stu_name);
            stuMobile=itemView.findViewById(R.id.stu_mobile);
            stuEmgMobile=itemView.findViewById(R.id.stu_emg_mobile);
            delete=itemView.findViewById(R.id.imgBtnDelete);
            edit=itemView.findViewById(R.id.imgBtnEdit);
            roomInfo=itemView.findViewById(R.id.imgBtnRoomInfo);
        }
    }
}
