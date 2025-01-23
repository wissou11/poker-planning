package com.example.PokerPlanningBack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="tasks")
public class TaskDTO {
    @Id
    private String id;
    private String title;
    private String description;
    private String projectId;
    private String sprintId;
    private String status;
    private int duree;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }



    public TaskDTO(String id, String nom, String description, String projectId, String sprintId, String status, int duree) {
        this.id = id;
        this.title = nom;
        this.description = description;
        this.projectId = projectId;
        this.sprintId = sprintId;
        this.status = status;
        this.duree = duree;
    }

    public TaskDTO() {
    }
}
