package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.exception.EstimationCollectionException;
import com.example.PokerPlanningBack.model.*;
import com.example.PokerPlanningBack.repository.IEstimationRepository;
import com.example.PokerPlanningBack.repository.TaskRepository;
import com.example.PokerPlanningBack.service.EstimationService;
import com.example.PokerPlanningBack.service.ScoreService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/estimations")
public class EstimationController {

    @Autowired
    private EstimationService estimationService;
    @Autowired
    private IEstimationRepository estimationRepository;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private TaskRepository taskRepository;
    @PostMapping("/create")
    public ResponseEntity<?>createEstimation(@RequestBody Estimation estimation){
        try {

            estimation.setStatus(Statut.ENABLE);
            estimation.setScores(new ArrayList<>());
            estimationService.createEstimation(estimation);
            return new ResponseEntity<Estimation>(estimation, HttpStatus.OK);

        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (EstimationCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?>getSingleEstimation(@PathVariable("id")String id){
        try {
            return new ResponseEntity<>(estimationService.getSingleEstimation(id),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEstimations() {
        List<Estimation> estimations = estimationService.getAllEstimations();
        return new ResponseEntity<>(estimations, !estimations.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<?>updateById(@PathVariable("id")String id,@RequestBody Estimation estimation){
        try {
            estimationService.updateEstimation(id,estimation);
            return new ResponseEntity<>("update estimation with id ="+id,HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (EstimationCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteById(@PathVariable("id")String id){
        try {
            estimationService.deleteEstimationById(id);
            return new ResponseEntity<>("succefully deleted with id "+id,HttpStatus.OK);
        }catch (EstimationCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}/average-score")
    public ResponseEntity<?> getAverageScore(@PathVariable("id") String id) {
        try {
            Estimation estimation = estimationService.getSingleEstimation(id);
            double averageScore = estimation.getAverageScore();
            return new ResponseEntity<>(averageScore, HttpStatus.OK);
        } catch (EstimationCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getEstimationByTaskId(@PathVariable String taskId) {
        List<Estimation> estimations = estimationRepository.findByTaskId(taskId);

        System.out.println("estimations: "+estimations.toArray().length);
        if (estimations.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(estimations);
            //return estimations;
        }
    }

    @PutMapping("/estimations/{id}")
    public ResponseEntity<?> updateEstimationDeadline(@PathVariable("id") String id, @RequestBody Map<String, Object> updates) {
        try {
            double deadline = Double.parseDouble(updates.get("deadline").toString());
            Optional<Estimation> estimationOptional = estimationRepository.findById(id);
            if (estimationOptional.isPresent()) {
                Estimation estimation = estimationOptional.get();
                estimation.setDeadline(deadline);
                estimationRepository.save(estimation);
                return ResponseEntity.ok("Estimation deadline updated");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estimation not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating estimation");
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateEstimationStatus(@PathVariable("id") String id, @RequestBody Map<String, String> updates) {
        try {
            String status = updates.get("status");
            Optional<Estimation> estimationOptional = estimationRepository.findById(id);
            if (estimationOptional.isPresent()) {
                Estimation estimation = estimationOptional.get();
                estimation.setStatus(Statut.valueOf(status));
                estimationRepository.save(estimation);
                return ResponseEntity.ok("Estimation status updated");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estimation not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating estimation status");
        }
    }

    @GetMapping("/status/enable")
    public ResponseEntity<?> getEstimationsWithStatusEnable() {
        List<Estimation> estimations = estimationRepository.findByStatus(Statut.ENABLE);
        return new ResponseEntity<>(estimations, !estimations.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/scores")
    public ResponseEntity<?> getScoresByEstimationId(@PathVariable("id") String id) {
        try {
            List<Score> scores = estimationService.getScoresByEstimationId(id);
            return new ResponseEntity<>(scores, HttpStatus.OK);
        } catch (EstimationCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable String taskId) {
        Optional<TaskDTO> task = taskRepository.findById(taskId);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
