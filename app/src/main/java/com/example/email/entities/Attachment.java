package com.example.email.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Base64;

@Entity
public class Attachment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "data")
    public Base64 data;

    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "name")
    public String name;

    public int messageId;
}
