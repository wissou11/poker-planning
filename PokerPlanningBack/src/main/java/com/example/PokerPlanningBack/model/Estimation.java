package com.example.PokerPlanningBack.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "estimations")
public class Estimation {



    @Id
    private String id ;
    @NonNull
    private String taskId;

    @NonNull

    private Statut status;
    @NonNull

    private Number deadline;



    private double averageScore;

    private List<Score> scores;// Ajouter une liste de scores

    // Méthode pour calculer la moyenne des scores
    public double getAverageScore() {
        return scores.stream().mapToInt(Score::getScoreValue).average().orElse(0.0);
    }

    // Méthode pour ajouter un nouveau score à l'estimation
    public void addScore(Score score) {
        this.scores.add(score);
    }





}
