package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.model.CommentDTO;
import com.example.PokerPlanningBack.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<?> getAllComments(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        List<CommentDTO> comments=commentRepository.findByProjectIdAndTaskId(projectId,taskId);
        if(comments.size()>0){
            return new ResponseEntity<List<CommentDTO>>(comments, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No comments available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/projects/{projectId}/tasks/{taskId}/comments/create")
    public ResponseEntity<?> createComment(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody CommentDTO comment){
        try {
            System.out.println("project id: "+ projectId);
            comment.setProjectId(projectId);
            comment.setTaskId(taskId);
            comment.setCreatedDate(LocalDateTime.now());
            commentRepository.save(comment);
            return new ResponseEntity<CommentDTO>(comment, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<?> getSingleComment(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId,@PathVariable("commentId") String commentId){
        Optional<CommentDTO> projectOptional=commentRepository.findByProjectIdAndTaskIdAndId(projectId,taskId,commentId);
        if(projectOptional.isPresent()){
            return new ResponseEntity<>(projectOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Project not found with id "+commentId, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<?> updateById(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId,@PathVariable("commentId") String commentId, @RequestBody CommentDTO commentDTO){
        Optional<CommentDTO> projectOptional=commentRepository.findByProjectIdAndTaskIdAndId(projectId,taskId,commentId);
        if(projectOptional.isPresent()){
            CommentDTO commentToSave=projectOptional.get();
            commentToSave.setContent(commentDTO.getContent()!=null ? commentDTO.getContent() : commentToSave.getContent());
            commentRepository.save(commentToSave);
            return new ResponseEntity<>(commentToSave,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Project not found with id "+commentId, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id){
        try {
            commentRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted comment with id "+id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
