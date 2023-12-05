package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    @FXML
    private void loginUser() {
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

//            statusLabel.setText("Welcome " + user.getLastName());
        } catch (Exception e) {
            //statusLabel.setText(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
