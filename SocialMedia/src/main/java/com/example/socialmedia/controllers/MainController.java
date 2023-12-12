// MainController.java
package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.service.MessageService;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button logInButton;
    private ServiceComponent service;

    @FXML
    private Button addUserButton;
    private MessageService messageService;

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialmedia/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        AnchorPane layout = loader.load();
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        LoginController controller = loader.getController();
        controller.setService(service);
        controller.setMessageService(messageService);

        stage.show();
    }
}

