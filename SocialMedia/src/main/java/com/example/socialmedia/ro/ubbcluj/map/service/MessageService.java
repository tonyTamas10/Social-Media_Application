package com.example.socialmedia.ro.ubbcluj.map.service;

import com.example.socialmedia.ro.ubbcluj.map.domain.Message;

import java.util.UUID;

public interface MessageService {

    boolean addMessage(Message message) throws ServiceException;

    Message deleteMessage(UUID id) throws ServiceException;

    Iterable<Message> getAll() throws ServiceException;
}
