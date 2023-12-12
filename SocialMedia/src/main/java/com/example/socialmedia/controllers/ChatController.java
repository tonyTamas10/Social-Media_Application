package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.Message;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.service.MessageService;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public class ChatController {
    @FXML
    public TextArea messageArea;
    @FXML
    public TextField messageField;
    @FXML
    public Button sendButton;
    @FXML
    public ListView<Message> messageListView;
    private ServiceComponent service;
    private MessageService messageService;
    private User user;
    private User friend;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void initApp(User user, User friend) {
        this.user = user;
        this.friend = friend;
        try {
            Collection<Message> allMessages = (Collection<Message>) messageService.getAll();
            messages.setAll(allMessages);
            initializeMessageList();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeMessageList() {
        messageListView.setItems(messages);
        messageListView.setCellFactory(message -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                } else {
                    setText(message.getText());
                }
            }
        });
    }

    @FXML
    public void sendMessage() {
        try {
            // TODO: add message to database
            String text = messageField.getText();
            Message message = new Message(UUID.randomUUID() , user.getId(), friend.getId(), text, LocalDateTime.now());
            messageService.addMessage(message);
            messages.add(message);
            initializeMessageList();
            messageField.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
