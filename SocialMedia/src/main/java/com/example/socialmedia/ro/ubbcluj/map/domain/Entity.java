package com.example.socialmedia.ro.ubbcluj.map.domain;

import java.io.Serializable;

public class Entity<UUID> implements Serializable {

   // private static final long serialVersionUID = 7331115341259248461L;
    protected transient UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}