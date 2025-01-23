package com.example.PokerPlanningBack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "comments")
public class CommentDTO {

    @Id
    private String id;
    private String taskId;
    private String projectId;
    private String content;
    private LocalDateTime createdDate;

    // Constructeurs, getters et setters
    public CommentDTO() {}

    public CommentDTO(String taskId, String projectId, String content, LocalDateTime createdDate) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.content = content;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
