package com.example.email.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MessageTag {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int messageId;

    public int tagId;
}
