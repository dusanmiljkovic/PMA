package com.example.email.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "smtp")
    public String smtp;

    @ColumnInfo(name = "pop_imap")
    public String popImap;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;
}
