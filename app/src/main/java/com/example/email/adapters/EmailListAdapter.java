package com.example.email.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.viewholders.EmailViewHolder;
import com.example.email.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class EmailListAdapter extends RecyclerView.Adapter<EmailViewHolder> implements Filterable {

    private final List<Message> messages;
    private final List<Message> messagesFull;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public EmailListAdapter(List<Message> messages) {
        this.messages = messages;
        messagesFull = new ArrayList<>(messages);
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_item, parent, false);
        return new EmailViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        final Message email = messages.get(position);
        holder.bind(email);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Message> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(messagesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Message message : messagesFull) {
                    if (message.subject.toLowerCase().contains(filterPattern)) {
                        filterList.add(message);
                    } else if (message.content.toLowerCase().contains(filterPattern)) {
                        filterList.add(message);
                    } else if (message.to.toLowerCase().contains(filterPattern)) {
                        filterList.add(message);
                    } else if (message.from.toLowerCase().contains(filterPattern)) {
                        filterList.add(message);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (messages != null) {
                messages.clear();
            }
            messages.addAll((List<Message>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
