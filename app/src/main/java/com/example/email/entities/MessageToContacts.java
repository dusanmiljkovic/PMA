package com.example.email.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class MessageToContacts {
    @Embedded
    public Message message;

    @Relation(
            parentColumn = "id",
            entity = Contact.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = MessageToCrossRef.class,
                    parentColumn = "messageId",
                    entityColumn = "contactId"
            )
    )
    public List<Contact> contacts;
}
