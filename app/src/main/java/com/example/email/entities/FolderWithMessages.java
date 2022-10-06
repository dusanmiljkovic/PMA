package com.example.email.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FolderWithMessages {
    @Embedded
    public Folder folder;
    @Relation(
            parentColumn = "id",
            entityColumn = "folderId"
    )

    public List<Message> messages;
}
