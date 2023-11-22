package com.example.socialmedia.ro.ubbcluj.map.repository.database;

import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseManager;
import com.example.socialmedia.ro.ubbcluj.map.domain.Entity;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.repository.InMemoryRepository;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;

import java.util.UUID;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    private String url;
    private String username;
    private String password;
    public DatabaseManager databaseManager;
    protected Repository<UUID, User> repo;

    protected AbstractDBRepository(String url, String username, String password, DatabaseManager databaseManager, Repository<UUID, User> repo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.databaseManager = databaseManager;
        this.repo = repo;
        loadData();
    }

    protected abstract void loadData();

    protected abstract Iterable<E> findAll_DB();
}
