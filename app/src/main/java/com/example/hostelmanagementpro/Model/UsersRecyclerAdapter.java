package com.example.hostelmanagementpro.Model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.hostelmanagementpro.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;


public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<Payment> paymentArrayList;
    DatabaseReference databaseReference;

    public UsersRecyclerAdapter(Context context, ArrayList<Payment> paymentArrayList) {
        this.context = context;
        this.paymentArrayList = paymentArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference("payments");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.pay_ment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Payment payment = paymentArrayList.get(position);

        holder.txtName.setText("Payment : " + payment.getName());
        holder.txtMonth.setText("Month : " + payment.getMonth());
        holder.txtPrice.setText("Price : " + payment.getPrice());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, payment.getId(), payment.getName(), payment.getMonth(), payment.getPrice());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, payment.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return paymentArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtMonth;
        TextView txtPrice;

        Button btnDelete;
        Button btnUpdate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtPrice = itemView.findViewById(R.id.txtPrice);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);

        }

    }

    public class ViewDialogUpdate {
        public void showDialog(Context context, String id, String name, String month, String price) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.view_dialog_confirm_delete);

            EditText txtName = dialog.findViewById(R.id.txtName);
            EditText txtMonth = dialog.findViewById(R.id.txtMonth);
            EditText txtPrice = dialog.findViewById(R.id.txtPrice);

            txtName.setText(name);
            txtMonth.setText(month);
            txtPrice.setText(price);

            Button btnUpdate = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            btnUpdate.setText("UPDATE");

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String newName = txtName.getText().toString();
                    String newMonth = txtMonth.getText().toString();
                    String newPrice = txtPrice.getText().toString();

                    if (name.isEmpty() || month.isEmpty() || price.isEmpty()) {
                        Toast.makeText(context, "Plrase Enter all data", Toast.LENGTH_SHORT).show();
                    } else {

                        if (newName.equals(name) && newMonth.equals(month) && newPrice.equals(price)) {
                            Toast.makeText(context, "You don't change anything", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("PAYMENT_TYPES").child(id).setValue(new Payment(id, newName, newMonth, newPrice));
                            Toast.makeText(context, "Payment Updated successfully !", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


                    }
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }

    public class ViewDialogConfirmDelete {
        public void showDialog(Context context, String id) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.view_dialog_confirm_delete);

            Button buttonDelete = dialog.findViewById(R.id.buttonDelete);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference.child("PAYMENT_TYPES").child(id).removeValue();
                    Toast.makeText(context, "Payment Delete successfully !", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }

}
