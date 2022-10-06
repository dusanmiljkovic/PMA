package com.example.email.entities;


import androidx.room.Entity;

@Entity(primaryKeys = {"messageId", "contactId"}
)
public class MessageToCrossRef {
    public int messageId;
    public int contactId;
}
