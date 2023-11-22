package com.example.socialmedia.ro.ubbcluj.map.repository.database;

import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseManager;
import com.example.socialmedia.ro.ubbcluj.map.domain.Entity;
import com.example.socialmedia.ro.ubbcluj.map.domain.Friendship;
import com.example.socialmedia.ro.ubbcluj.map.domain.Tuple;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FriendshipDBRepository extends AbstractDBRepository<Tuple<UUID, UUID>, Friendship> {

    public FriendshipDBRepository(DatabaseManager databaseManager, Repository<UUID, User> repo) {
        this(databaseManager.getUrl(), databaseManager.getUsername(), databaseManager.getPassword(), databaseManager, repo);
    }

    public FriendshipDBRepository(String url, String username, String password, DatabaseManager databaseManager, Repository<UUID, User> repo) {
        super(url, username, password, databaseManager, repo);
    }

    @Override
    protected void loadData() {
        findAll_DB().forEach(entity -> {
            try {
                super.save(entity);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Optional<Friendship> findOne(Tuple<UUID, UUID> ids) throws RepositoryException {

        Optional<Friendship> friendship = Optional.empty();

        try (Connection connection = databaseManager.getConnection(); // estabilishing a connection with the database
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships' " + "WHERE user1_id = " + " '" + ids.getLeft().toString() + "'" + " AND user2_id = " + "'" + ids.getRight().toString() + "'" + ";"); // preparing a query to be executed
             ResultSet resultSet = statement.executeQuery() // execute the query
        ){

            if(resultSet.next()) {

                UUID id1 = UUID.fromString(resultSet.getString("user1_id"));
                UUID id2 = UUID.fromString(resultSet.getString("user2_id"));
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("friendsFrom"));

                var entity = new Entity<Tuple<UUID, UUID>>();
                entity.setId(new Tuple<>(id1, id2));
                friendship = Optional.of(new Friendship(entity, date, repo));
            }
            return friendship;
            
        } catch (Exception e) {
            throw new RepositoryException(e.toString());
        }

    }
    
    public Iterable<Friendship> findAll_DB() {
        Set<Friendship> friendships = new HashSet<>();
        Friendship friendship;
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships;");
            ResultSet resultSet = statement.executeQuery()
        ){

            while(resultSet.next()) {

                UUID id1 = UUID.fromString(resultSet.getString("user1_id"));
                UUID id2 = UUID.fromString(resultSet.getString("user2_id"));
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Timestamp timestamp = resultSet.getTimestamp("friendsfrom");
                LocalDateTime date = timestamp.toLocalDateTime();

                Optional<User> user1 = repo.findOne(id1);
                Optional<User> user2 = repo.findOne(id2);

                var entity = new Entity<Tuple<UUID, UUID>>();
                entity.setId(new Tuple<>(id1, id2));
                friendship = new Friendship(entity, date, repo);

                Friendship finalFriendship = friendship;
                user1.ifPresent(user -> user.addFriend(finalFriendship.getUser2()));
                user2.ifPresent(user -> user.addFriend(finalFriendship.getUser1()));

                friendships.add(friendship);
            }
            
            return friendships;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) throws RepositoryException {
        Optional<Friendship> friendship = super.save(entity);

        try (Connection connection = databaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO friendships VALUES ( " + "'" + entity.getUser1().getId().toString() + "'" + ", " + "'" +entity.getUser2().getId().toString() + "'" + ", '" + Timestamp.valueOf(entity.getDate()) + "');")
        ){
            int rowsAffected = statement.executeUpdate();
            
            if(rowsAffected > 0) {
                entity.getUser1().addFriend(entity.getUser2());
                entity.getUser2().addFriend(entity.getUser1());
                return friendship;
            }
            else 
                throw new RepositoryException("There are no values to insert into the table");
        } catch (Exception e) {
            throw new RepositoryException(e.toString());
        }
    }

    @Override
    public Optional<Friendship> delete(Tuple<UUID, UUID> id) throws RepositoryException {
        Optional<Friendship> entity = super.delete(id);
        if(entity.isPresent()) {
            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE from friendships where user1_id = '" + id.getLeft().toString() + "' AND user2_id = '" + id.getRight().toString() + "';");
                 ResultSet resultSet = statement.executeQuery()) {

            } catch (Exception e) {
                throw new RepositoryException(e.toString());
            }
        }
        return  entity;
    }
}
