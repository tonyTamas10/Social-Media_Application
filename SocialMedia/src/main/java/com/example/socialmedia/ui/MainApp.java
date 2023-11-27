// MainApp.java
package com.example.socialmedia.ui;

import com.example.socialmedia.controllers.MainController;
import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseConfig;
import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseManager;
import com.example.socialmedia.ro.ubbcluj.map.domain.Friendship;
import com.example.socialmedia.ro.ubbcluj.map.domain.Tuple;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.domain.validators.UserValidator;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;
import com.example.socialmedia.ro.ubbcluj.map.repository.database.FriendshipDBRepository;
import com.example.socialmedia.ro.ubbcluj.map.repository.database.UserDBRepository;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class MainApp extends Application {

    private ServiceComponent service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager(DatabaseConfig.DB_URL, DatabaseConfig.DB_USER, DatabaseConfig.DB_PASS);
        Repository<UUID, User> userRepository = new UserDBRepository(databaseManager);
        Repository<Tuple<UUID, UUID>, Friendship> friendshipRepository = new FriendshipDBRepository(databaseManager, userRepository);
        UserValidator userValidator = new UserValidator();
        service = new ServiceComponent(userValidator, userRepository, friendshipRepository);

        initView(stage);
        stage.show();
    }

    private void initView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialmedia/main.fxml"));
        AnchorPane layout = loader.load();
        stage.setScene(new Scene(layout));
        stage.setTitle("Social Media");

        MainController controller = loader.getController();
        controller.setService(this.service);
    }
}
