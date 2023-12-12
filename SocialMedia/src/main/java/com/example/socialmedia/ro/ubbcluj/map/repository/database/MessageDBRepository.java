package com.example.socialmedia.ro.ubbcluj.map.repository.database;

import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseManager;
import com.example.socialmedia.ro.ubbcluj.map.domain.Message;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MessageDBRepository extends AbstractDBRepository<UUID, Message>{
    public MessageDBRepository(DatabaseManager databaseManager, Repository<UUID, User> repo) {
        this(databaseManager.getUrl(), databaseManager.getUsername(), databaseManager.getPassword(), databaseManager, repo);
    }
    public MessageDBRepository(String url, String username, String password, DatabaseManager databaseManager, Repository<UUID, User> repo) {
        super(url, username, password, databaseManager, repo);
    }

    @Override
    protected void loadData() {
        findAll_DB().forEach(entity -> {
            try {
                super.save(entity);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @Override
    protected Iterable<Message> findAll_DB() {
        Set<Message> messages = new HashSet<>();
        try (var connection = databaseManager.getConnection();
             var statement = connection.prepareStatement("SELECT * FROM messages");
             var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("message_id"));
                UUID senderId = UUID.fromString(resultSet.getString("sender_id"));
                UUID receiverId = UUID.fromString(resultSet.getString("receiver_id"));
                String text = resultSet.getString("message_content");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("message_from"));
                messages.add(new Message(id, senderId, receiverId, text, date));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    @Override
    public Optional<Message> save(Message entity) {
        try (var connection = databaseManager.getConnection();
             var statement = connection.prepareStatement("INSERT INTO messages(message_id, sender_id, receiver_id, message_content, message_from) VALUES (?, ?, ?, ?, ?)");
        ) {
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getSenderID().toString());
            statement.setString(3, entity.getReceiverID().toString());
            statement.setString(4, entity.getText());
            statement.setTimestamp(5, entity.getTimeSent());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
}
