package com.siphy.siphy.Service.Exceptions;

public class UsernameAlreadyExists extends RuntimeException{
    public UsernameAlreadyExists(String message){
        super(message);
    }
}
