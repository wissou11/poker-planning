package com.example.PokerPlanningBack.service;

import com.example.PokerPlanningBack.exception.EstimationCollectionException;
import com.example.PokerPlanningBack.exception.ScoreCollectionException;
import com.example.PokerPlanningBack.model.Estimation;
import com.example.PokerPlanningBack.model.Score;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface IScoreService {
    void createScore(Score score)throws ConstraintViolationException, ScoreCollectionException;

    List<Score> getAllScores();

    Score getSingleScore(String id)throws ScoreCollectionException;

    void updateScore(String id,Score score)throws ScoreCollectionException;

    void deleteScoreById(String id)throws ScoreCollectionException;

}
