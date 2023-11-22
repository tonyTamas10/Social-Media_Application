package com.example.socialmedia.ro.ubbcluj.map.domain;

import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


public class Friendship extends Entity<Tuple<UUID, UUID>> {

    LocalDateTime date;
    private final User user1;

    private final User user2;

    public Friendship(Entity<Tuple<UUID, UUID>> userTuple, Repository<UUID, User> repo) throws ServiceException, RepositoryException {
        this.user1 = repo.findOne(userTuple.getId().getLeft()).get();
        this.user2 = repo.findOne(userTuple.getId().getRight()).get();
        this.setId(new Tuple<>(user1.getId(), user2.getId()));
        this.date = LocalDateTime.now();
    }

    public Friendship(Entity<Tuple<UUID, UUID>> userTuple, LocalDateTime date, Repository<UUID, User> repo) throws ServiceException, RepositoryException {
        this.user1 = repo.findOne(userTuple.getId().getLeft()).get();
        this.user2 = repo.findOne(userTuple.getId().getRight()).get();
        this.setId(new Tuple<>(user1.getId(), user2.getId()));
        this.date = date;
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
