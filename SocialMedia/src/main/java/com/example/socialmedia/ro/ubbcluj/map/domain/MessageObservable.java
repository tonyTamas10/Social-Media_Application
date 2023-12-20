package com.example.socialmedia.ro.ubbcluj.map.domain;

import java.util.ArrayList;
import java.util.List;

public class MessageObservable {
    private final List<MessageObserver> observers = new ArrayList<>();

    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Message message) {
        observers.forEach(observer -> observer.update(message));
    }

    public boolean hasObserver(MessageObserver observer) {
        return observers.contains(observer);
    }
}
