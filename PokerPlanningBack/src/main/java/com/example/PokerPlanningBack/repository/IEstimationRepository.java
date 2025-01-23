package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.Estimation;
import com.example.PokerPlanningBack.model.Statut;
import com.example.PokerPlanningBack.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IEstimationRepository extends MongoRepository<Estimation,String> {
    @Query("{'status': ?0}")
    List<Estimation> findByStatus(Statut status);
   // @Query("{'_id': ?0}")
    Optional<Estimation> findById(String id);


    // Explicitly adding findAll method, though it's already inherited from MongoRepository
    List<Estimation> findAll();

    List<Estimation> findByTaskId(String taskId);
}
