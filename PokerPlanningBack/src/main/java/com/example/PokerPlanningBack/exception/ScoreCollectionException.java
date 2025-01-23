package com.example.PokerPlanningBack.exception;

public class ScoreCollectionException extends Exception{

    private static final long serialVersionUID=1L;

    public ScoreCollectionException(String message){super(message);}

    public static String NotFoundException(String id){return "Score with "+id+" not found";}

    public static String ScoreAlreadyExists(){return "Score with given name already exists";}
}
