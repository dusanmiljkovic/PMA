package com.example.email.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.ContactListAdapter;
import com.example.email.models.Contact;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    private final ImageView icon;
    private final TextView name;

    public ContactViewHolder(@NonNull View itemView, ContactListAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        icon = itemView.findViewById(R.id.contact_item_icon);
        name = itemView.findViewById(R.id.contact_item_name);

        itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public void bind(Contact contact) {
        icon.setImageResource(R.mipmap.ic_launcher);
        name.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
    }

}
