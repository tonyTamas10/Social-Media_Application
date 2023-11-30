package com.example.socialmedia.ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message extends Entity<UUID> {

    private String text;
    private User sender;
    private User receiver;
    private LocalDateTime timeSent;

    public Message(String text, User sender, User receiver) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.timeSent = LocalDateTime.now();
        this.setId(UUID.randomUUID());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
}
