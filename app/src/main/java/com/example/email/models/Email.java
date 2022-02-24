package com.example.email.models;

import java.util.Date;

import lombok.Data;

@Data
public class Email {
    private Long id;
    private String from;
    private String to;
    private Date dateTime;
    private String subject;
    private String content;

    public Email(Long id, String from, String to, Date dateTime, String subject, String content) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
