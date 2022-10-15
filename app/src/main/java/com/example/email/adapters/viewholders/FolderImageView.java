package com.example.email.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.activities.FoldersActivity;
import com.example.email.adapters.FolderListAdapter;
import com.example.email.database.MailDatabase;
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

    public void bind(Folder folder) {
        title.setText(folder.name);
        MailDatabase db = MailDatabase.getDbInstance(itemView.getContext());
        int messagesCount = db.messageDao().countByFolderId(folder.id);
        messagesSum.setText(String.valueOf(messagesCount));
    }

}
