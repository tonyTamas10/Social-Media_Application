package com.example.socialmedia.ro.ubbcluj.map.repository;

public class RepositoryException extends Exception{
    private final String exception;
    public RepositoryException(String exception) {
        super(exception);
        this.exception = exception;
    }

    @Override
    public String toString() {
        return exception;
    }
}
