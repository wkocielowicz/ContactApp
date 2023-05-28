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
import com.example.contactappuz.logic.PhotoManager;

import java.util.List;

/**
 * Adapter for displaying Contact items in a RecyclerView.
 */
public class ContactRowAdapter extends RecyclerView.Adapter<ContactRowAdapter.MyViewHolder> {

    private Context context;
    private List<Contact> contactList;
    private OnItemClickListener onItemClickListener;

    /**
     * Interface for handling item click events.
     */
    public interface OnItemClickListener {
        void onUpdateButtonClick(Contact contact);

        void onDeleteButtonClick(Contact contact);

        void onNavigateButtonClick(Contact contact);
    }

    /**
     * Constructs a ContactRowAdapter with the given contact list and item click listener.
     *
     * @param contactList          The list of contacts to display.
     * @param onItemClickListener The item click listener.
     */
    public ContactRowAdapter(List<Contact> contactList, OnItemClickListener onItemClickListener) {
        this.contactList = contactList;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Updates the contact list with new data.
     *
     * @param updatedContacts The updated contact list.
     */
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
        holder.address.setText(contact.getAddress().getAddress());
        holder.birthDate.setText(contact.getBirthDate());
        holder.category.setText(contact.getCategory());

        // Load and display contact's photo
        PhotoManager.loadImageFromDevice(context, contact, bitmap -> {
            holder.photo.setImageBitmap(bitmap);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    /**
     * ViewHolder class for holding the views of a contact row item.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, address, birthDate, category;
        Button deleteButton, updateButton, navigateButton;
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
            navigateButton = itemView.findViewById(R.id.navigateButton);
            photo = itemView.findViewById(R.id.photoView);

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

            navigateButton.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    Contact contact = contactList.get(getAdapterPosition());
                    onItemClickListener.onNavigateButtonClick(contact);
                }
            });
        }
    }
}
