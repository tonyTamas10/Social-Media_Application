package com.example.socialmedia.ro.ubbcluj.map.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DatabaseManager.java
public class DatabaseManager {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
