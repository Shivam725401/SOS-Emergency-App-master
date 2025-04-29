package com.example.sos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<ContactModel> modelArrayList;
    DatabaseHelper databaseHelper;

    public ContactRecyclerAdapter(Context context, ArrayList<ContactModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ContactRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerAdapter.ViewHolder holder, int position) {
        ContactModel model = modelArrayList.get(position);

        holder.name.setText(model.getName());
        holder.number.setText(model.getNumber());

        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this contact?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        int id = Integer.parseInt(model.getId());
                        boolean checkData = databaseHelper.deleteContact(id);
                        if (checkData) {
                            modelArrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, modelArrayList.size());
                            Toast.makeText(context, "Contact deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerContactName);
            number = itemView.findViewById(R.id.recyclerContactNumber);
        }
    }
}
