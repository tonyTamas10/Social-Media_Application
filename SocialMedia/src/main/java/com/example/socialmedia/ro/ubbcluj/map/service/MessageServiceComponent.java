package com.example.socialmedia.ro.ubbcluj.map.service;

import com.example.socialmedia.ro.ubbcluj.map.domain.Message;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class MessageServiceComponent implements MessageService{
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
        ArrayList<Message> messages;
        try {
            messages = (ArrayList<Message>) messageRepo.findAll();

            messages.sort(Comparator.comparing(Message::getTimeSent));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return messages;
    }
}
