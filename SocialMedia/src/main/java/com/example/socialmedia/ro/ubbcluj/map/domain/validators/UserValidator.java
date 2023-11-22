package com.example.socialmedia.ro.ubbcluj.map.domain.validators;

import com.example.socialmedia.ro.ubbcluj.map.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String error = "";

        if (entity.getId() == null)
            error += "Id must not be null\n";
        if(entity.getFirstName().equals(""))
            error += "First name must not be null\n";
        if(entity.getLastName().equals(""))
            error += entity.getClass().toString() + " must have a last name\n";

        if(!error.isEmpty())
            throw new ValidationException(error);
    }
}

