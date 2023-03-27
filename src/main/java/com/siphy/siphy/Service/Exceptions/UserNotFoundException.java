package com.siphy.siphy.Service.Exceptions;

import com.siphy.siphy.Model.User;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}
