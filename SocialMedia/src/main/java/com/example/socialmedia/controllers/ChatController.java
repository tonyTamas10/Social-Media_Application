package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.Message;
import com.example.socialmedia.ro.ubbcluj.map.domain.MessageObserver;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;
import com.example.socialmedia.ro.ubbcluj.map.service.MessageServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class ChatController implements MessageObserver {
    @FXML
    public TextArea messageArea;
    @FXML
    public TextField messageField;
    @FXML
    public Button sendButton;
    @FXML
    public ListView<Message> messageListView;
    @FXML
    public Button backButton;
    private ServiceComponent service;
    private MessageServiceComponent messageService;
    private User user;
    private User friend;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    public void setMessageService(MessageServiceComponent messageService) {
        this.messageService = messageService;
    }

    public void initApp(User user, User friend) {
        this.user = user;
        this.friend = friend;

        if(!messageService.hasObserver(this)) {
            messageService.addObserver(this);
        }

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
        messageListView.setCellFactory(message -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String senderName = null;
                    String text = null;
                    Image senderImage = null;
                    try {
                        senderName = service.findOne(message.getSenderID()).isPresent() ?  service.findOne(message.getSenderID()).get().getFirstName() + " " + service.findOne(message.getSenderID()).get().getLastName() : null;
                        text = message.getText();
                        senderImage = new Image(Objects.requireNonNull(getClass().getResource("/Images/user.png")).toExternalForm());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Creating components for cell
                    Text senderText = new Text(senderName);
                    Text messageText = new Text(text);
                    ImageView senderImageView = new ImageView(senderImage);

                    // Styling components if needed
                    // (You might want to set the size and styles based on your design)
                    senderImageView.setFitHeight(20);
                    senderImageView.setFitWidth(20);
                    senderText.setStyle("-fx-font-weight: bold;");

                    // Creating an HBox to contain sender's name, profile picture, and the message
                    HBox cellContent = new HBox(10, senderImageView, senderText, messageText);

                    // Setting the HBox as the graphic for the cell
                    setGraphic(cellContent);
                }
            }
        });
    }

    @FXML
    public void sendMessage() {
        try {
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

    @FXML
    public void onBackButtonClick(ActionEvent event) throws ServiceException, RepositoryException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialmedia/friends.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        AnchorPane layout = loader.load();
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        FriendsController controller = loader.getController();
        controller.setService(service);
        controller.setMessageService(messageService);
        controller.initApp(user);
        controller.initializeFriendRequestsTable();
        controller.initializeFriendsTable(); // calling method from here to have the service initialized

        stage.show();
    }

    @Override
    public void update(Message message) {
        if (message.getSenderID().equals(friend.getId())) {
            messages.add(message);
            initializeMessageList();
        }
    }
}
