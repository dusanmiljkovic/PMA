package com.example.email.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.FolderListAdapter;
import com.example.email.entities.Folder;
import com.example.email.entities.FolderWithMessages;

public class FolderImageView extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView messagesSum;

    public FolderImageView(@NonNull View itemView, FolderListAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        title = itemView.findViewById(R.id.folder_item_title);
        messagesSum = itemView.findViewById(R.id.folder_item_number);

        itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public void bind(FolderWithMessages folderWithMessages) {
        title.setText(folderWithMessages.folder.name);
        messagesSum.setText(String.valueOf(folderWithMessages.messages.size()));
    }

}
