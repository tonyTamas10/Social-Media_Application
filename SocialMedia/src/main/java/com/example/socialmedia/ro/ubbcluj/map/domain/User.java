package com.example.socialmedia.ro.ubbcluj.map.domain;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class User extends Entity<UUID> {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Map<UUID, User> friends;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.setId(UUID.randomUUID());
        this.friends = new HashMap<>();
    }

    public User(UUID id, String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = "parola";
        this.setId(id);
        this.friends = new HashMap<>();
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = "parola";
        this.setId(UUID.randomUUID());
        this.friends = new HashMap<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Iterable<User> getFriends() {
        return friends.values();
    }

    public void setFriends(Map<UUID, User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user) {
        friends.put(user.getId(), user);
    }

    public void removeFriend(User user) {
        friends.remove(user.getId(), user);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, email, password, friends);
    }
}