package com.example.socialmedia.ro.ubbcluj.map.domain;

import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.example.socialmedia.ro.ubbcluj.map.domain.FriendshipRequest.APPROVED;


public class Friendship extends Entity<Tuple<UUID, UUID>> {

    LocalDateTime date;
    private final User user1;

    private final User user2;
    private FriendshipRequest requstState;

    public Friendship(Entity<Tuple<UUID, UUID>> userTuple, Repository<UUID, User> repo) throws RepositoryException {
        this.user1 = repo.findOne(userTuple.getId().getLeft()).get();
        this.user2 = repo.findOne(userTuple.getId().getRight()).get();
        this.setId(new Tuple<>(user1.getId(), user2.getId()));
        this.date = LocalDateTime.now();
        this.requstState = APPROVED;
    }

    public Friendship(Entity<Tuple<UUID, UUID>> userTuple, LocalDateTime date, Repository<UUID, User> repo) throws RepositoryException {
        this.user1 = repo.findOne(userTuple.getId().getLeft()).get();
        this.user2 = repo.findOne(userTuple.getId().getRight()).get();
        this.setId(new Tuple<>(user1.getId(), user2.getId()));
        this.date = date;
        this.requstState = APPROVED;
    }

    public Friendship(Entity<Tuple<UUID, UUID>> userTuple, LocalDateTime date, Repository<UUID, User> repo, FriendshipRequest request) throws RepositoryException {
        this.user1 = repo.findOne(userTuple.getId().getLeft()).get();
        this.user2 = repo.findOne(userTuple.getId().getRight()).get();
        this.setId(new Tuple<>(user1.getId(), user2.getId()));
        this.date = date;
        this.requstState = request;
    }

    public Friendship(String email1, String email2, LocalDateTime date, ServiceComponent service, FriendshipRequest request) throws RepositoryException, ServiceException {
        this.user1 = service.getUserByEmail(email1);
        this.user2 = service.getUserByEmail(email2);
        this.setId(new Tuple<>(user1.getId(), user2.getId()));
        this.date = date;
        this.requstState = request;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FriendshipRequest getRequstState() {
        return requstState;
    }

    public void setRequstState(FriendshipRequest requstState) {
        this.requstState = requstState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user1, user2);
    }
}
