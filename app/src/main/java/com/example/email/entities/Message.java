package com.example.email.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.email.entities.converters.DateConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "message_number")
    public int messageNumber;

    @ColumnInfo(name = "received_date")
    public Date receivedDate;

    @ColumnInfo(name = "subject")
    public String subject;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "from")
    public String from;

    @ColumnInfo(name = "to")
    public String to;

    public int folderId;

    public int accountId;

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReceivedDateString() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(receivedDate.getTime()), ZoneId.systemDefault()).toString();
    }
}
