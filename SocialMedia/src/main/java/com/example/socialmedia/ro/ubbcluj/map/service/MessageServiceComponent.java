package com.example.socialmedia.ro.ubbcluj.map.service;

import com.example.socialmedia.ro.ubbcluj.map.domain.Message;
import com.example.socialmedia.ro.ubbcluj.map.domain.MessageObservable;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class MessageServiceComponent extends MessageObservable implements MessageService{
    private final Repository<UUID, Message> messageRepo;

    public MessageServiceComponent(Repository<UUID, Message> messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    public boolean addMessage(Message message) throws ServiceException {
        Optional<Message> returnedMessage;
        try {
            returnedMessage = messageRepo.save(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        if (returnedMessage.isPresent()) {
            throw new ServiceException("Message could not be saved");
        }

        notifyObservers(message);

        return true;
    }

    @Override
    public Message deleteMessage(UUID id) throws ServiceException {
        Optional<Message> returnedMessage;
        try {
            returnedMessage = messageRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return returnedMessage.orElse(null);
    }

    @Override
    public Iterable<Message> getAll() throws ServiceException {
        try {
            return StreamSupport.stream(messageRepo.findAll().spliterator(), false).sorted(Comparator.comparing(Message::getTimeSent)).toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
