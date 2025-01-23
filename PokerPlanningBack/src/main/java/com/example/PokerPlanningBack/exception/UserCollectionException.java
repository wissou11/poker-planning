package com.example.PokerPlanningBack.exception;
/**
 * Rayen Benoun
 */
public class UserCollectionException extends Exception{
    private static final long  serialVersionUID=1L;
    public UserCollectionException(String message){super(message);}

    public static String NotFoundException(String id){return "User with "+id+" not found";}

    public static String UserAlreadyExists(){return "User with given name already exists";}
}
