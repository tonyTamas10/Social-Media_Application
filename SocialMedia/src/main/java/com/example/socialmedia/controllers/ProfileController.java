package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    private ServiceComponent service;

    @FXML
    protected void onFriendsButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialmedia/friends.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        AnchorPane layout = loader.load();
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        stage.show();
    }

    public void setService(ServiceComponent service) {
        this.service = service;
    }
}
