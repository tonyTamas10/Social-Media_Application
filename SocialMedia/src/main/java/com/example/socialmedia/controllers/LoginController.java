package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private ListView<User> users;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private Label statusLabel;
    @FXML
    private PasswordField passwordField;

    private ServiceComponent service;

    private void addUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            User newUser = new User(firstName, lastName, email, password);
            service.save(newUser);
            statusLabel.setText("User added: " + newUser);
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}
