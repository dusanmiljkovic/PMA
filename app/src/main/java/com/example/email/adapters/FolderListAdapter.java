package com.example.email.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.viewholders.FolderImageView;
import com.example.email.entities.Folder;
import com.example.email.entities.FolderWithMessages;

import java.util.List;

public class FolderListAdapter extends RecyclerView.Adapter<FolderImageView> {

    private final List<FolderWithMessages> folderWithMessagesList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FolderListAdapter(List<FolderWithMessages> folderWithMessagesList) {
        this.folderWithMessagesList = folderWithMessagesList;
    }

    @NonNull
    @Override
    public FolderImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_list_item, parent, false);
        return new FolderImageView(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderImageView holder, int position) {
        FolderWithMessages folder = folderWithMessagesList.get(position);
        holder.bind(folder);
    }

    @Override
    public int getItemCount() {
        return folderWithMessagesList.size();
    }

}
