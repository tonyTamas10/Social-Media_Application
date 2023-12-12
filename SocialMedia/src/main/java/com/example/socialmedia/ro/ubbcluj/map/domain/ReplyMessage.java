package com.example.socialmedia.ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReplyMessage extends Message{
    private Message message;

    public ReplyMessage(UUID messageID, UUID senderID, UUID receiverID, String text, LocalDateTime timeSent, Message message) {
        super(messageID, senderID, receiverID, text, timeSent);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
