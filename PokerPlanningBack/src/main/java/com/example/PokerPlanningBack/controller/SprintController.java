package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.model.ProjectDTO;
import com.example.PokerPlanningBack.model.SprintDTO;
import com.example.PokerPlanningBack.model.TaskDTO;
import com.example.PokerPlanningBack.repository.SprintRepository;
import com.example.PokerPlanningBack.service.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SprintController {
    @Autowired
    private SprintRepository sprintRepository;

    @GetMapping("/projects/{projectId}/sprints")
    public ResponseEntity<?> getAllSprints(@PathVariable("projectId") String projectId){
        List<SprintDTO> sprints= sprintRepository.findByProjectId(projectId);
        if(sprints.size()>0){
            return new ResponseEntity<List<SprintDTO>>(sprints, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No sprints available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/projects/{projectId}/sprints")
    public ResponseEntity<?> createSprint(@RequestBody SprintDTO sprint, @PathVariable("projectId") String projectId){
        try {
            sprint.setProjectId(projectId);
            sprintRepository.save(sprint);
            return new ResponseEntity<SprintDTO>(sprint, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projects/{projectId}/sprints/{sprintId}")
    public ResponseEntity<?> getSingleSprint(@PathVariable("projectId") String projectId, @PathVariable("sprintId") String sprintId){
        Optional<SprintDTO> sprintOptional = sprintRepository.findByProjectIdAndId(projectId,sprintId);
        if(sprintOptional.isPresent()){
            return new ResponseEntity<>(sprintOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Sprint not found with id "+sprintId, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/projects/{projectId}/sprints/{sprintId}")
    public ResponseEntity<?> updateSprintById(@PathVariable("projectId") String projectId, @PathVariable("sprintId") String sprintId, @RequestBody SprintDTO sprintDTO){
        Optional<SprintDTO> sprintOptional = sprintRepository.findByProjectIdAndId(projectId,sprintId);
        if(sprintOptional.isPresent()){
            SprintDTO sprintToSave= sprintOptional.get();
            sprintToSave.setStatus(sprintDTO.getStatus()!=null ? sprintDTO.getStatus() : sprintToSave.getStatus());
            sprintToSave.setNom(sprintDTO.getNom()!=null ? sprintDTO.getNom() : sprintToSave.getNom());
            sprintToSave.setDateFin(sprintDTO.getDateFin()!=null ? sprintDTO.getDateFin() : sprintToSave.getDateFin());
            sprintRepository.save(sprintToSave);
            return new ResponseEntity<>(sprintToSave,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Sprint not found with id "+sprintId, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/projects/{projectId}/sprints/{sprintId}")
    public ResponseEntity<?> deleteSprintById(@PathVariable("projectId") String projectId,@PathVariable("sprintId") String sprintId){
        try {
            sprintRepository.deleteByProjectIdAndId(projectId,sprintId);
            return new ResponseEntity<>("Successfully deleted sprint with id "+sprintId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
