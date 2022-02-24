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
import com.example.email.models.Email;

import java.util.ArrayList;
import java.util.List;

public class EmailListAdapter extends RecyclerView.Adapter<EmailViewHolder> implements Filterable {

    private final List<Email> emails;
    private final List<Email> emailsFull;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public EmailListAdapter(List<Email> emails) {
        this.emails = emails;
        emailsFull = new ArrayList<>(emails);
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_item, parent, false);
        return new EmailViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        final Email email = emails.get(position);
        holder.bind(email);
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Email> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(emailsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Email email : emailsFull) {
                    if (email.getSubject().toLowerCase().contains(filterPattern)) {
                        filterList.add(email);
                    } else if (email.getContent().toLowerCase().contains(filterPattern)) {
                        filterList.add(email);
                    } else if (email.getTo().toLowerCase().contains(filterPattern)) {
                        filterList.add(email);
                    } else if (email.getFrom().toLowerCase().contains(filterPattern)) {
                        filterList.add(email);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (emails != null) {
                emails.clear();
            }
            emails.addAll((List<Email>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
