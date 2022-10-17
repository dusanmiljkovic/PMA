package com.example.email.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.viewholders.EmailViewHolder;
import com.example.email.adapters.viewholders.LoadingViewHolder;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class EmailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final List<Message> messages;
    private final List<Message> messagesFull;
    private OnItemClickListener onItemClickListener;
    private MailDatabase db;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public EmailListAdapter(List<Message> messages, Context content) {
        this.messages = messages;
        messagesFull = new ArrayList<>(messages);
        db = MailDatabase.getDbInstance(content);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_item, parent, false);
            return new EmailViewHolder(view, onItemClickListener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof EmailViewHolder) {
            populateItemRows((EmailViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return messages.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
            List<Message> allMails = db.messageDao().getAll(false);


            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(messagesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                try {
                    for (Message message : allMails) {
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
                } catch (Exception e){
                    System.out.println("An error occurred.");
                    e.printStackTrace();
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

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(EmailViewHolder viewHolder, int position) {
        final Message email = messages.get(position);
        viewHolder.bind(email);
    }

}
