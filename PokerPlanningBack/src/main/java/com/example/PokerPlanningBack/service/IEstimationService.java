package com.example.PokerPlanningBack.service;

import com.example.PokerPlanningBack.exception.EstimationCollectionException;
import com.example.PokerPlanningBack.model.Estimation;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface IEstimationService {

    void createEstimation(Estimation estimation)throws ConstraintViolationException, EstimationCollectionException;

    List<Estimation>getAllEstimations();

    Estimation getSingleEstimation(String id)throws EstimationCollectionException;

    void updateEstimation(String id,Estimation estimation)throws EstimationCollectionException;

    void deleteEstimationById(String id)throws EstimationCollectionException;


}
