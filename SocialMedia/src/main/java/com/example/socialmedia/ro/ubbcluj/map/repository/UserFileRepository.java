package com.example.socialmedia.ro.ubbcluj.map.repository;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;

import java.util.List;
import java.util.UUID;

public class UserFileRepository extends AbstractFileRepository<UUID, User> {

    public UserFileRepository(String fileName) throws RepositoryException {
        super(fileName);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        //TODO: implement method

        return new User(attributes.get(1),attributes.get(2), attributes.get(3), attributes.get(4));
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName();
    }
}
