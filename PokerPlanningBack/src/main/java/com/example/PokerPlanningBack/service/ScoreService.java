package com.example.PokerPlanningBack.service;

import com.example.PokerPlanningBack.exception.EstimationCollectionException;
import com.example.PokerPlanningBack.exception.ScoreCollectionException;
import com.example.PokerPlanningBack.model.Estimation;
import com.example.PokerPlanningBack.model.Score;
import com.example.PokerPlanningBack.repository.IEstimationRepository;
import com.example.PokerPlanningBack.repository.IScoreRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreService implements IScoreService{

    @Autowired
    private IScoreRepository scoreRepository;

    @Autowired
    private IEstimationRepository estimationRepository;
    @Override
    public void createScore(Score score) throws ConstraintViolationException, ScoreCollectionException {
        Optional<Score> scoreOptional=scoreRepository.findById(score.getId());
        if(scoreOptional.isPresent()){
            throw new ScoreCollectionException(ScoreCollectionException.ScoreAlreadyExists());
        }else {
            score.setCreatedAt(new Date(System.currentTimeMillis()));
            scoreRepository.save(score);
        }
    }


    @Override
    public List<Score> getAllScores() {
        List<Score> scores=scoreRepository.findAll();
        if (scores.size()>0){
            return scores;
        }else {
            return new ArrayList<Score>();
        }
    }

    @Override
    public Score getSingleScore(String id) throws ScoreCollectionException {
        Optional<Score>scoreOptional=scoreRepository.findById(id);
        if (scoreOptional.isPresent()){
            return scoreOptional.get();
        }else {
            throw new ScoreCollectionException(ScoreCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void updateScore(String id, Score score) throws ScoreCollectionException {
        Optional<Score>scoreOptional=scoreRepository.findById(id);
        if (scoreOptional.isPresent()){
            Score scoreUpdate=scoreOptional.get();
            scoreUpdate.setScoreValue(score.getScoreValue());
            scoreUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            scoreRepository.save(scoreUpdate);
        }else{
            throw new ScoreCollectionException(ScoreCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteScoreById(String id) throws ScoreCollectionException {
        Optional<Score>scoreOptional=scoreRepository.findById(id);
        if (!scoreOptional.isPresent()){
            throw new ScoreCollectionException(ScoreCollectionException.NotFoundException(id));
        }else {
            scoreRepository.deleteById(id);
        }
    }

    public void submitScore(Score score) throws EstimationCollectionException,ScoreCollectionException {

        Optional<Estimation> estimationOptional = estimationRepository.findById(score.getEstimationId());

        //loop through the scores
        if (estimationOptional.isPresent()) {

            for (Score s : estimationOptional.get().getScores()) {
                if (s.getUserId().equals(score.getUserId())) {
                    throw new ScoreCollectionException("user has already submitted a score for this estimation");
                }
            }

            Estimation estimation = estimationOptional.get();
            estimation.addScore(score);
            estimationRepository.save(estimation);

        } else {
            throw new EstimationCollectionException(EstimationCollectionException.NotFoundException(score.getEstimationId()));
        }
    }

    // validation supplémentaire qui vérifie si la date limite (deadline) d'une estimation n'est pas dépassée avant d'ajouter un score

/*
    public void submitScore(Score score) throws EstimationCollectionException, ScoreCollectionException {
        // Vérifier si le score existe déjà pour cet utilisateur et cette estimation
        Optional<Score> existingScore = scoreRepository.findByUserIdAndEstimationId(score.getUserId(), score.getEstimationId());
        if (existingScore.isPresent()) {
            throw new ScoreCollectionException("User has already submitted a score for this estimation.");
        }

        // Récupérer l'estimation associée
        Optional<Estimation> estimationOptional = estimationRepository.findById(score.getEstimationId());
        if (estimationOptional.isPresent()) {
            Estimation estimation = estimationOptional.get();

            // Vérifier si la date limite de l'estimation est dépassée
            long currentTime = System.currentTimeMillis();
            if (currentTime > estimation.getDeadline().longValue()) {
                throw new ScoreCollectionException("Cannot submit score. The deadline for this estimation has passed.");
            }

            // Ajouter le score à l'estimation
            estimation.addScore(score);
            estimationRepository.save(estimation);
        } else {
            throw new EstimationCollectionException(EstimationCollectionException.NotFoundException(score.getEstimationId()));
        }
    }*/
}
