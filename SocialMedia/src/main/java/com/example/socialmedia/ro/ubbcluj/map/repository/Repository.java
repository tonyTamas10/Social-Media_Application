package com.example.socialmedia.ro.ubbcluj.map.repository;

import com.example.socialmedia.ro.ubbcluj.map.domain.Entity;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;

import java.util.Optional;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */

public interface Repository<ID, E extends Entity<ID>> {

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     */
    Optional<E> findOne(ID id) throws RepositoryException;

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll() throws RepositoryException;

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     */
    Optional<E> save(E entity) throws RepositoryException;


    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    Optional<E> delete(ID id) throws RepositoryException;

    void setServiceComponent(ServiceComponent service);
}




