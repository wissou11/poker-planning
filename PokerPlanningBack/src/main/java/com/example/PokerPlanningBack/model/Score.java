package com.example.PokerPlanningBack.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "scores")
public class Score {

    @Id
    private String id;

    @NonNull
    private String userId;

    @NonNull
    private int scoreValue;

    @NonNull
    private String estimationId;

    private User user;

    // Optionnel : Date de création ou de mise à jour du score
    private Date createdAt;
    private Date updatedAt;

    public Score(String userId, int scoreValue, String estimationId) {
        this.userId = userId;
        this.scoreValue = scoreValue;
        this.estimationId = estimationId;
        this.createdAt = new Date();
    }
}
