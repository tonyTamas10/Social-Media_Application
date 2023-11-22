// MainApp.java
package com.example.socialmedia.ui;

import com.example.socialmedia.controllers.MainController;
import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseConfig;
import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseManager;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.domain.validators.UserValidator;
import com.example.socialmedia.ro.ubbcluj.map.repository.database.FriendshipDBRepository;
import com.example.socialmedia.ro.ubbcluj.map.repository.database.UserDBRepository;
import com.example.socialmedia.ro.ubbcluj.map.service.Service;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class MainApp extends Application {

    private Service<UUID, User> service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initializeService();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setService(service);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Social Network App");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeService() {
        DatabaseManager databaseManager = new DatabaseManager(DatabaseConfig.DB_URL, DatabaseConfig.DB_USER, DatabaseConfig.DB_PASS);
        UserDBRepository userRepository = new UserDBRepository(databaseManager);
        FriendshipDBRepository friendshipRepository = new FriendshipDBRepository(databaseManager, userRepository);
        UserValidator userValidator = new UserValidator();
        service = new ServiceComponent(userValidator, userRepository, friendshipRepository);
    }
}
