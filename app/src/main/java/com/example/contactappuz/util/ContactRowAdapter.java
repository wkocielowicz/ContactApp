package com.example.contactappuz.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactappuz.R;
import com.example.contactappuz.database.model.Contact;

import java.util.List;

public class ContactRowAdapter extends RecyclerView.Adapter<ContactRowAdapter.MyViewHolder> {

    private Context context;
    private List<Contact> contactList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onUpdateButtonClick(Contact contact);
        void onDeleteButtonClick(Contact contact);
    }

    public ContactRowAdapter(List<Contact> contactList, OnItemClickListener onItemClickListener) {
        this.contactList = contactList;
        this.onItemClickListener = onItemClickListener;
    }

    public void updateContacts(List<Contact> updatedContacts) {
        this.contactList = updatedContacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        holder.address.setText(contact.getAddress());
        holder.birthDate.setText(contact.getBirthDate());
        holder.category.setText(contact.getCategory());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, address, birthDate, category;
        Button deleteButton, updateButton;
        ImageView photo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.textFirstName);
            lastName = itemView.findViewById(R.id.textLastName);
            address = itemView.findViewById(R.id.textAddress);
            birthDate = itemView.findViewById(R.id.textBirthDate);
            category = itemView.findViewById(R.id.textCategory);

            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.update_button);
            photo = itemView.findViewById(R.id.photoView);
            //photo.setImageResource(R.drawable.logo);
            //photo.setImageURI(Uri.parse("res/drawable/logo.png"));

            deleteButton.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    Contact contact = contactList.get(position);
                    onItemClickListener.onDeleteButtonClick(contact);
                }
            });

            updateButton.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    Contact contact = contactList.get(getAdapterPosition());
                    onItemClickListener.onUpdateButtonClick(contact);
                }
            });
        }
    }
}