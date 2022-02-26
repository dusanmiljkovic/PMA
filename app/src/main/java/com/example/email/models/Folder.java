package com.example.email.models;

import lombok.Data;

@Data
public class Folder {

    private Long id;
    private String name;
    private int messages;

    public Folder(Long id, String name, int messages) {
        this.id = id;
        this.name = name;
        this.messages = messages;
    }

    public Long id(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }
}
