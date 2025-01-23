package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.Estimation;
import com.example.PokerPlanningBack.model.Score;
import com.example.PokerPlanningBack.model.Statut;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IScoreRepository extends MongoRepository<Score,String> {

    @Query("{'_id': ?0}")



    Optional<Score> findById(String id);

    Optional<Score> findByUserIdAndEstimationId(String userId, String estimationId);
}
