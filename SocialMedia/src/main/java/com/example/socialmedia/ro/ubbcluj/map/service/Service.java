package com.example.socialmedia.ro.ubbcluj.map.service;

import com.example.socialmedia.ro.ubbcluj.map.domain.Entity;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;

import java.util.Optional;

public interface Service<ID, E extends Entity<ID>> {
    /**
     * @param id - id of the entity to be returned
     * @return E - the entity with the specified id
     *             or null if the entity doesn't exist
     */
    Optional<E> findOne(ID id) throws ServiceException, RepositoryException;

    /**
     * @return all entities
     */
    Iterable<E> findAll() throws ServiceException, RepositoryException;

    /**
     * @param entity - the entity to be saved, must not be null
     * @throws ServiceException - if the entity is already in the list or is null
     */
    void save(E entity) throws ServiceException, RepositoryException;

    /**
     * @param id - the id of the entity that will be deleted
     *           must not be null
     * @return - the deleted entity if everything work well
     * @throws ServiceException - if the id is null
     */
    Optional<E> delete(ID id) throws ServiceException, RepositoryException;
}
