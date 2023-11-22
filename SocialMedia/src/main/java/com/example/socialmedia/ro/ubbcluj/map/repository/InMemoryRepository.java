package com.example.socialmedia.ro.ubbcluj.map.repository;

import com.example.socialmedia.ro.ubbcluj.map.domain.Entity;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    Map<ID,E> entities;
    public ServiceComponent service;

    public InMemoryRepository() {
        entities= new HashMap<>();
    }

    public void setServiceComponent(ServiceComponent service) {
        this.service = service;
    }

    @Override
    public Optional<E> findOne(ID id) throws RepositoryException {
        E entity = entities.get(id);
        if(entity != null) {
            return Optional.of(entity);
        } else {
            throw new RepositoryException("There is no entity with this id");
        }
    }

    @Override
    public Iterable<E> findAll() throws RepositoryException {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) throws RepositoryException {
        if(entities.get(entity.getId()) != null) { // if entity already exists return entity
            return Optional.of(entity);
        }
        else entities.putIfAbsent(entity.getId(),entity);
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) throws RepositoryException {
        Optional<E> entity = findOne(id);
        entities.remove(id);
        return entity;
    }
}
