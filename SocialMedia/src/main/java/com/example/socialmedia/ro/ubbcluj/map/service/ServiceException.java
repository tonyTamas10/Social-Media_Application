package com.example.socialmedia.ro.ubbcluj.map.service;

public class ServiceException extends Exception{
    private final String errorMessage;

    public ServiceException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
