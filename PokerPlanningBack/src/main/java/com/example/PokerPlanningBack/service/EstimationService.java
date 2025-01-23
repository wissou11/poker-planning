package com.example.PokerPlanningBack.service;

import com.example.PokerPlanningBack.exception.EstimationCollectionException;
import com.example.PokerPlanningBack.model.Estimation;
import com.example.PokerPlanningBack.model.Score;
import com.example.PokerPlanningBack.model.Statut;
import com.example.PokerPlanningBack.model.User;
import com.example.PokerPlanningBack.repository.IEstimationRepository;
import com.example.PokerPlanningBack.repository.IUserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class EstimationService implements IEstimationService {

    @Autowired
    private IEstimationRepository estimationRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void createEstimation(Estimation estimation) throws ConstraintViolationException, EstimationCollectionException {
        estimationRepository.save(estimation);
    }

    @Override
    public List<Estimation> getAllEstimations() {
        return estimationRepository.findAll();
    }

    @Override
    public Estimation getSingleEstimation(String id) throws EstimationCollectionException {
        Optional<Estimation> estimationOptional = estimationRepository.findById(id);
        if (estimationOptional.isPresent()) {
            Estimation estimation = estimationOptional.get();

            // Fetch and set scores with user information for the estimation
            List<Score> scores = estimation.getScores();
            if (scores != null && !scores.isEmpty()) {
                scores.forEach(score -> {
                    Optional<User> userOptional = userRepository.findById(score.getUserId());
                    userOptional.ifPresent(score::setUser); // Assuming Score has a setUser method
                });
            }

            // Calculate and set the average score
            double averageScore = estimation.getAverageScore();
            estimation.setAverageScore(averageScore); // Ensure Estimation has a setAverageScore method

            return estimation;
        } else {
            throw new EstimationCollectionException(EstimationCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void updateEstimation(String id, Estimation estimation) throws EstimationCollectionException {
        Optional<Estimation> estimationOptional = estimationRepository.findById(id);
        if (estimationOptional.isPresent()) {
            Estimation estimationUpdate = estimationOptional.get();
            estimationUpdate.setStatus(estimation.getStatus());
            estimationUpdate.setDeadline(estimation.getDeadline());
            estimationRepository.save(estimationUpdate);
        } else {
            throw new EstimationCollectionException(EstimationCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteEstimationById(String id) throws EstimationCollectionException {
        if (estimationRepository.existsById(id)) {
            estimationRepository.deleteById(id);
        } else {
            throw new EstimationCollectionException(EstimationCollectionException.NotFoundException(id));
        }
    }

    public List<Estimation> getEstimationsByStatus(Statut status) {
        return estimationRepository.findByStatus(status);
    }

    public List<Score> getScoresByEstimationId(String estimationId) throws EstimationCollectionException {
        Optional<Estimation> estimationOptional = estimationRepository.findById(estimationId);
        if (estimationOptional.isPresent()) {
            return estimationOptional.get().getScores();
        } else {
            throw new EstimationCollectionException(EstimationCollectionException.NotFoundException(estimationId));
        }
    }
}

