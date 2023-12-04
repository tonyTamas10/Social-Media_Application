package com.example.socialmedia.controllers;

import com.example.socialmedia.ro.ubbcluj.map.domain.Friendship;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;
import com.example.socialmedia.ro.ubbcluj.map.service.MessageService;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FriendsController {

    @FXML
    public Button profileButton;

    @FXML
    public Button addButton;

    private final ListView<User> listView = new ListView<>();

    @FXML
    private ListView<Friendship> friendshipListView;

    private ServiceComponent service;
    private User user;
    private MessageService messageService;

    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<String> friendsRequests = FXCollections.observableArrayList();
    private final ObservableList<String> friendRequestsSend = FXCollections.observableArrayList();
    private final ObservableList<String> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // this is empty so when the load method is called it won't use uninitialized components
    }

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    public void initApp(User user) {

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

    public void initializeTable() throws ServiceException, RepositoryException {

    }

    public void initializeFriendsTable() throws ServiceException, RepositoryException {
        Collection<Friendship> friendshipCollection = (Collection<Friendship>) service.findAllFriendships();

        List<Friendship> friendshipsList = new ArrayList<>(friendshipCollection);

        //friendshipListView.getItems().addAll((Friendship) friendshipsList.stream().filter(friendship -> friendship.getRequstState().toString().equals("PENDING")));
        friendshipListView.getItems().addAll(friendshipsList);
        friendshipListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Friendship friendship, boolean empty) {
                super.updateItem(friendship, empty);
                if (empty || friendship == null) {
                    setText(null);
                } else {
                    setText(friendship.getUser1().getFirstName() + " " + friendship.getUser1().getLastName());
                }
            }
        });
    }

    @FXML
    public void onAddButtonClick(ActionEvent event) throws ServiceException, RepositoryException {
        Dialog<ListView<User>> dialog = new Dialog<>();

        Collection<User> userCollection = (Collection<User>) service.findAll();

        List<User> userList = new ArrayList<>(userCollection);

        listView.getItems().addAll(userList);

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getFirstName() + " " + user.getLastName() + " " + user.getEmail()); // Set the appropriate user property
                }
            }
        });

        dialog.setGraphic(listView);
        dialog.showAndWait();
        //TODO: trebuie sa fac sa se inchida fereastra la click
    }
}
