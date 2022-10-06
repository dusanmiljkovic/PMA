package com.example.email.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "display_name")
    public String displayName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "show")
    public Boolean show;

    public int accountId;
}
