package com.example.email.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AccountWithMessages {
    @Embedded
    public Account account;
    @Relation(
            parentColumn = "id",
            entityColumn = "accountId"
    )

    public List<Message> messages;
}
