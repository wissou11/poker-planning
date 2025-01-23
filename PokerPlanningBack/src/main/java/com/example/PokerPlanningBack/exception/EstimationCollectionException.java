package com.example.PokerPlanningBack.exception;

public class EstimationCollectionException extends Exception{

    private static final long serialVersionUID=1L;

    public EstimationCollectionException(String message){super(message);}

    public static String NotFoundException(String id){return "Estimation with "+id+" not found";}

    public static String EstimationAlreadyExists(){return "Estimation with given name already exists";}
}
