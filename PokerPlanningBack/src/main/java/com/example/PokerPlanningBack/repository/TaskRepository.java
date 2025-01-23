package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.Estimation;
import com.example.PokerPlanningBack.model.TaskDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<TaskDTO,String> {
    List<TaskDTO> findBySprintId(String sprintId);

    Optional<TaskDTO> findById(String id);
    List<TaskDTO> findByProjectId(String projectId);
    Optional<TaskDTO> findByProjectIdAndId(String projectId,String taskId);
    Optional<TaskDTO> findByProjectIdAndSprintIdAndId(String projectId, String sprintId, String taskId);
    void deleteByProjectIdAndSprintIdAndId(String projectId, String sprintId, String taskId);
    void deleteByProjectIdAndId(String projectId, String taskId);
}
