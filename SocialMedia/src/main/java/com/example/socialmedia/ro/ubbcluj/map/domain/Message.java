package com.example.socialmedia.ro.ubbcluj.map.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Message extends Entity<UUID> {

    private UUID messageID;
    private String text;
    private UUID senderID;
    private UUID receiverID;
    private Timestamp timeSent;

    public Message(UUID messageID, UUID senderID, UUID receiverID, String text, LocalDateTime timeSent) {
        this.messageID = messageID;
        this.text = text;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.timeSent = Timestamp.valueOf(LocalDateTime.now());
        this.setId(UUID.randomUUID());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getMessageID() {
        return messageID;
    }

    public void setMessageID(UUID messageID) {
        this.messageID = messageID;
    }

    public UUID getSenderID() {
        return senderID;
    }

    public void setSenderID(UUID senderID) {
        this.senderID = senderID;
    }

    public UUID getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(UUID receiverID) {
        this.receiverID = receiverID;
    }

    public Timestamp getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = Timestamp.valueOf(timeSent);
    }
}
