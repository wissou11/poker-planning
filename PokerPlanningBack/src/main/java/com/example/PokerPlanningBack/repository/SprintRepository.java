package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.SprintDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends MongoRepository<SprintDTO,String> {
    List<SprintDTO> findByProjectId(String projectId);
    Optional<SprintDTO> findByProjectIdAndId(String projectId, String id);
    void deleteByProjectIdAndId(String projectId, String sprintId);
}
