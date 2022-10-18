package com.example.email.adapters.viewholders;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.EmailListAdapter;
import com.example.email.entities.Message;

import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EmailViewHolder extends RecyclerView.ViewHolder {

    private final ImageView senderImage;
    private final TextView tSender;
    private final TextView tSubject;
    private final TextView tContent;
    private final TextView tDateTime;

    public EmailViewHolder(@NonNull View itemView, EmailListAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        senderImage = itemView.findViewById(R.id.email_item_sender_icon);
        tSender = itemView.findViewById(R.id.email_item_sender);
        tSubject = itemView.findViewById(R.id.email_item_subject);
        tContent = itemView.findViewById(R.id.email_item_text);
        tDateTime = itemView.findViewById(R.id.email_item_date_time);

        itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public void bind(Message message) {
        senderImage.setBackgroundResource(R.mipmap.ic_launcher);
        if (message.from != null)
            tSender.setText(message.from.split(" <")[0]);
        if (message.subject != null)
            tSubject.setText(message.subject);
        if (message.content != null){
            String displayText = message.textIsHtml ? Jsoup.parse(message.content).body().text() : message.content;
            tContent.setText(displayText);
        }
        if (message.receivedDate != null)
            tDateTime.setText(message.getReceivedDateString());
    }

}
