package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.CommentDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<CommentDTO,String> {
    List<CommentDTO> findByProjectIdAndTaskId(String projectId, String taskId);
    Optional<CommentDTO> findByProjectIdAndTaskIdAndId(String projectId, String taskId, String commentId);
}
