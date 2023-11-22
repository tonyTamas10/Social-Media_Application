package com.example.socialmedia.ro.ubbcluj.map.repository.database;

import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseManager;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UserDBRepository extends AbstractDBRepository<UUID, User> {

    public UserDBRepository(DatabaseManager databaseManager) {
       this(databaseManager.getUrl(), databaseManager.getUsername(), databaseManager.getPassword(), databaseManager);
    }

    public UserDBRepository(String url, String username, String password, DatabaseManager databaseManager) {
        super(url, username, password, databaseManager, null);
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
    public Optional<User> findOne(UUID uuid) throws RepositoryException {
        try(Connection connection = databaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from users " + " where user_id = '" + uuid.toString() + "'")
            )
        {

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {

                UUID id = UUID.fromString(resultSet.getString("user_id"));
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");

                return Optional.of(new User(id, firstName, lastName, email));
            }

        } catch (Exception e) {
            throw new RepositoryException(e.toString());
        }

        return Optional.empty();
    }

//    @Override
//    public Iterable<User> findAll() throws RepositoryException {
//        Set<User> users = new HashSet<>();
//        try (Connection connection = DriverManager.getConnection(url, username, password);
//             PreparedStatement statement = connection.prepareStatement("SELECT u1.user_id AS user_id, u1.firstName AS user_firstName, u1.lastName AS user_lastName, u1.email AS user_email, u2.user_id AS friend_id, u2.firstName AS friend_firstName, u2.lastName AS friend_lastName, u2.email AS friend_email FROM friendships f LEFT JOIN users u1 ON f.user1_id = u1.user_id LEFT JOIN users u2 ON f.user2_id = u2.user_id;");
//             ResultSet resultSet = statement.executeQuery()) {
//
//            while (resultSet.next()) {
//                UUID id = UUID.fromString(resultSet.getString("user_id"));
//                String firstName = resultSet.getString("user_firstName");
//                String lastName = resultSet.getString("user_lastName");
//                String email = resultSet.getString("user_email");
//
//                UUID id2 = UUID.fromString(resultSet.getString("friend_id"));
//                String firstNameFriend = resultSet.getString("friend_firstName");
//                String lastNameFriend = resultSet.getString("friend_lastName");
//                String emailFriend = resultSet.getString("friend_email");
//
//                User user = new User(id, firstName, lastName, email);
//                User friend = new User(id2, firstNameFriend, lastNameFriend, emailFriend);
//
//                user.addFriend(friend);
//                friend.addFriend(user);
//                users.add(user);
//            }
//
//            return users;
//
//        } catch (Exception e) {
//            throw new RepositoryException(e.toString());
//        }
//    }

    public Iterable<User> findAll_DB() {
        Set<User> users = new HashSet<>();
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users;");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                User user = new User(firstName, lastName, email);
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) throws RepositoryException {
        Optional<User> user = super.save(entity);

        if(user.isEmpty()) {

            try (Connection connection = databaseManager.getConnection();
                 Statement statement = connection.createStatement()) {

                String sqlQuery = "INSERT INTO users " + "VALUES(" + "'" + entity.getId() + "', '" + entity.getFirstName() + "', '"
                        + entity.getLastName() + "', '" + entity.getEmail() + "');";
                statement.execute(sqlQuery);

            } catch (Exception e) {
                throw new RepositoryException(e.toString());
            }
        }

        return user;
    }

    @Override
    public Optional<User> delete(UUID id) throws RepositoryException {
        Optional<User> entity = super.delete(id);
        if(entity.isPresent()) {
            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE from users where user_id = '" + id.toString() + "';");
                 ResultSet resultSet = statement.executeQuery()) {

            } catch (Exception e) {
                throw new RepositoryException(e.toString());
            }
        }
        return  entity;
    }

}
