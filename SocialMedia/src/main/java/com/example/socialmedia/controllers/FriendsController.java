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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FriendsController {

    @FXML
    public Button profileButton;

    @FXML
    public Button addButton;

    private final ListView<User> listView = new ListView<>();
    @FXML
    public Button logoutButton;
    @FXML
    public Button removeButton;

    @FXML
    private ListView<String> friendshipListView;
    @FXML
    private ListView<Pair<String, String>> friendRequestsListView;

    private ServiceComponent service;
    private User user;
    private MessageService messageService;

    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<Pair<String, String>> friendsRequests = FXCollections.observableArrayList();
    private final ObservableList<String> friendRequestsSend = FXCollections.observableArrayList();
    private final ObservableList<String> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // this is empty so when the load method is called it won't use uninitialized components
    }

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    public void initApp(User user) throws ServiceException, RepositoryException {
        this.user = user;

        // finding all friends of the user
        service.findAllFriendships().forEach(friendship -> {
            if(friendship.getRequstState().toString().equals("APPROVED")) {
                if (friendship.getUser1().getId().equals(user.getId())) {
                    User friend = friendship.getUser2();
                    friends.add(friend.getFirstName() + " " + friend.getLastName() + " " + friend.getEmail());
                } else if(friendship.getUser2().getId().equals(user.getId())) {
                    User friend = friendship.getUser1();
                    friends.add(friend.getFirstName() + " " + friend.getLastName() + " " +friend.getEmail());
                }
            }
        });

        // finding all the friend requests for the user that logged in
        service.findAllFriendships().forEach(friendship -> {
            if(friendship.getUser2().getId().equals(user.getId()) && (Objects.equals(friendship.getRequstState().toString(), "PENDING"))) {
                    User friend = friendship.getUser1();
                    friendsRequests.add(new Pair<>(friend.getFirstName() + " " + friend.getLastName(), friend.getEmail()));

            }
        });

        // finding all the users other than the user that logged in
        service.findAll().forEach(user1 -> {
            if(!user1.getId().equals(user.getId())) {
                users.add(user1.getFirstName() + " " + user1.getLastName() + " " + user1.getEmail());
            }
        });
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

    public void initializeFriendRequestsTable() {
        friendRequestsListView.getItems().removeAll(friendsRequests);
        friendRequestsListView.getItems().addAll(friendsRequests);

        friendRequestsListView.setCellFactory(param -> new ListCell<>() {
            private final Button acceptRequest = new Button("Accept");
            private final Button declineRequest = new Button("Decline");
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getKey());
                    ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/user.png"))));
                    imageView.setFitHeight(25);
                    imageView.setFitWidth(25);
                    setGraphic(new HBox(acceptRequest, declineRequest, imageView));

                    acceptRequest.setOnAction(event -> {
                        try {
                            service.acceptFriendship(user.getEmail(), item.getValue());

                            friendsRequests.remove(item);
                            friends.add(item.getKey());

                            setText(null);
                            setGraphic(null);
                            initializeFriendsTable();
                        } catch (ServiceException e) {
                            System.out.println(e.getMessage());
                        }
                    });

                    declineRequest.setOnAction(event -> {
                        try {
                            service.declineFriendship(user.getEmail(), item.getValue());

                            friendsRequests.remove(item);

                            setText(null);
                            setGraphic(null);
                        } catch (ServiceException e) {
                            System.out.println(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void initializeFriendsTable() {

        friendshipListView.getItems().removeAll(friends);
        friendshipListView.getItems().addAll(friends);
        friendshipListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String friend, boolean empty) {
                super.updateItem(friend, empty);
                if (empty || friend == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String[] name = friend.split(" ");
                    Text email = new Text(name[2]);
                    email.setStyle("-fx-opacity: 0;");
                    setText(name[0] + " " + name[1] + " " + email);
                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Images/user.png")));
                    imageView.setFitHeight(25);
                    imageView.setFitWidth(25);
                    setGraphic(imageView);
                }
            }
        });
    }

    @FXML
    public void onAddButtonClick(ActionEvent event) throws ServiceException, RepositoryException {

        Collection<User> userCollection = (Collection<User>) service.findAll();

        List<User> userList = new ArrayList<>(userCollection);

        listView.getItems().removeAll(userList);
        listView.getItems().addAll(userList);

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(user.getFirstName() + " " + user.getLastName() + " " + user.getEmail()); // Set the appropriate user property
                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Images/user.png")));
                    imageView.setFitHeight(25);
                    imageView.setFitWidth(25);
                    setGraphic(imageView);
                }
            }
        });

        // setting up a dialog for the add friend button from the main screen
        Stage stage = new Stage();
        stage.setTitle("Add User");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());

        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(listView, 0, 0, 2, 1);

        Button addFriendButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        addFriendButton.setOnAction(e -> { // setting an action for the add friend button

            User selectedUser = listView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {

                Friendship potentialFriendship = null;
                try {
                    // creating a friendship object with the request state PENDING
                    potentialFriendship = service.sendFriendRequest(this.user.getEmail(), selectedUser.getEmail());
                } catch (ServiceException ex) {
                    System.out.println("Error adding a friend");
                }

                if(potentialFriendship != null) {
                    this.friendRequestsSend.add(potentialFriendship.getUser2().getEmail());
                }
            }
            stage.close();
        });
        // implementing the cancel button functionality
        cancelButton.setOnAction(e -> stage.close());

        gridPane.add(addFriendButton, 0, 1);
        gridPane.add(cancelButton, 1, 1);

        Scene scene = new Scene(gridPane, 400, 400);

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void onLogOutButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/socialmedia/login.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            stage.setScene(scene);

            LoginController controller = loader.getController();
            controller.setService(service);

            stage.show();
        } catch (Exception ignored) {
            // let the app do nothing if it cant go back to log in
        }
    }

    @FXML
    public void onRemoveButtonClick() {
        try {
            // getting the email of the user
            String selection = friendshipListView.getSelectionModel().getSelectedItem();
            String[] selectionSplit = selection.split(" ");
            String email = selectionSplit[selectionSplit.length - 1];

            // deleting the friendship with the selected friend
            service.deleteFriendship(service.getUserByEmail(email), service.getUserByEmail(this.user.getEmail()));

            friends.remove(selectionSplit[0] + " " + selectionSplit[1]);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Friendship Update");
            alert.setHeaderText(null);
            alert.setContentText("You are no longer friends with this user.");

            alert.showAndWait();

            initializeFriendsTable();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
