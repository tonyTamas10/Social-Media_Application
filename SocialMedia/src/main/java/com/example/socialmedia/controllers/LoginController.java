package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.service.MessageServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private Label statusLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    private ServiceComponent service;
    private MessageServiceComponent messageService;

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    public void setMessageService(MessageServiceComponent messageService) {
        this.messageService = messageService;
    }

    @FXML
    private void loginUser(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            User user = service.getUserByEmail(email);

            if(user == null) {
                throw new ServiceException("User with this email doesn't exist");
            }

            boolean userFound = false;
            for(User tempUser : service.findAll()) {
                if (tempUser.getPassword().equals(password)) {
                    userFound = true;
                    break;
                }
            }

            if(!userFound) {
                throw new ServiceException("Wrong password");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText("Welcome, " + user.getLastName() + "!");
            alert.setContentText("You have successfully logged in.");
            alert.showAndWait();

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

//            statusLabel.setText("Welcome " + user.getLastName());
        } catch (Exception e) {
            //statusLabel.setText(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
