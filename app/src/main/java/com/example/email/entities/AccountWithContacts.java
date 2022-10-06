package com.example.email.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AccountWithContacts {
    @Embedded
    public Account account;
    @Relation(
            parentColumn = "id",
            entityColumn = "accountId"
    )

    public List<Contact> contacts;
}
