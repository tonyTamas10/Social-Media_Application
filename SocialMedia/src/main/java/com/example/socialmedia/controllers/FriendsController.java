package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FriendsController {

    @FXML
    public Button profileButton;

    @FXML
    private ListView<User> listView;

    private ServiceComponent service;


    public void setService(ServiceComponent service) {
        this.service = service;
    }

    @FXML
    protected void onProfileButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialmedia/profile.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        AnchorPane layout = loader.load();
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        ProfileController controller = loader.getController();
        controller.setService(service);

        stage.show();
    }

    public void initialize() throws ServiceException, RepositoryException {
        // Initialize ListView with users
        List<User> userList = (List<User>) service.findAll(); // Assuming getAll() retrieves all users
        listView.getItems().addAll(userList);

        // Set a custom cell factory if needed
        listView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getFirstName()); // Set the appropriate user property
                }
            }
        });
    }
}
