package com.example.email.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.email.entities.converters.DateConverter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "message_number")
    public long messageNumber;

    @ColumnInfo(name = "received_date")
    public Date receivedDate;

    @ColumnInfo(name = "subject")
    public String subject;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "text_is_html")
    public boolean textIsHtml;

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
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(receivedDate.getTime()), ZoneId.systemDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        if (formatter.format(receivedDate).equals(formatter.format(Date.from(Instant.now())))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm");
            return ldt.format(dtf);
        }
        else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d LLL yy");
            return ldt.format(dtf);
        }
    }
}
