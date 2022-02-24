package com.example.email.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.EmailListAdapter;
import com.example.email.models.Email;

public class EmailViewHolder extends RecyclerView.ViewHolder {

    private final ImageView senderImage;
    private final TextView sender;
    private final TextView subject;
    private final TextView content;
    private TextClock dateTime;

    public EmailViewHolder(@NonNull View itemView, EmailListAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        senderImage = itemView.findViewById(R.id.email_item_sender_icon);
        sender = itemView.findViewById(R.id.email_item_sender);
        subject = itemView.findViewById(R.id.email_item_subject);
        content = itemView.findViewById(R.id.email_item_text);
        dateTime = itemView.findViewById(R.id.email_item_date_time);

        itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public void bind(Email email) {
        senderImage.setBackgroundResource(R.mipmap.ic_launcher);
        sender.setText(email.getFrom());
        subject.setText(email.getSubject());
        content.setText(email.getContent());
//        dateTime
    }

}
