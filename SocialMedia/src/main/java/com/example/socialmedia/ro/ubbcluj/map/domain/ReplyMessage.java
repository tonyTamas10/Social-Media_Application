package com.example.socialmedia.ro.ubbcluj.map.domain;

public class ReplyMessage extends Message{
    private Message message;

    public ReplyMessage(String text, User sender, User receiver, Message message) {
        super(text, sender, receiver);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
