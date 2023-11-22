// MainController.java
package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.UUID;

public class MainController {

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

    @FXML
    private void initialize() {
        addUserButton.setOnAction(event -> addUser());
    }

    public void setService(Service<UUID, User> service) {
        this.service = service;
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

