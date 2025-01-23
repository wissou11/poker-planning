package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.exception.EstimationCollectionException;
import com.example.PokerPlanningBack.exception.ScoreCollectionException;
import com.example.PokerPlanningBack.model.Estimation;
import com.example.PokerPlanningBack.model.Score;
import com.example.PokerPlanningBack.service.ScoreService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;


    @PostMapping("/create")
    public ResponseEntity<?> createScore(@RequestBody Score score){
        try {
            scoreService.createScore(score);
            return new ResponseEntity<Score>(score, HttpStatus.OK);

        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (ScoreCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?>getSingleScore(@PathVariable("id")String id){
        try {
            return new ResponseEntity<>(scoreService.getSingleScore(id),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<?>updateById(@PathVariable("id")String id,@RequestBody Score score){
        try {
            scoreService.updateScore(id,score);
            return new ResponseEntity<>("update score with id ="+id,HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (ScoreCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteById(@PathVariable("id")String id){
        try {
            scoreService.deleteScoreById(id);
            return new ResponseEntity<>("succefully deleted with id "+id,HttpStatus.OK);
        }catch (ScoreCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitScore(@RequestBody Score score) {
        try {
            scoreService.submitScore(score);
            return new ResponseEntity<>("Score submitted successfully", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EstimationCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (ScoreCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
