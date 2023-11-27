// MainController.java
package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class MainController {

    @FXML
    private Button logInButton;
    private Service<UUID, User> service;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button addUserButton;

    @FXML
    private Label statusLabel;

    public void setService(Service<UUID, User> service) {
        this.service = service;
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialmedia/appWindow.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        AnchorPane layout = loader.load();
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        stage.show();
    }

    private void addUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        try {
            User newUser = new User(firstName, lastName, email);
            service.save(newUser);
            statusLabel.setText("User added: " + newUser);
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}

