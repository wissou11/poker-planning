package com.example.PokerPlanningBack.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Document(collection="projects")
public class ProjectDTO {
    @Id
    private String id;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date enddate;
    private String status;
    private String creator;
    private List<String> developers;
    //private int nbSprints;
    //private int nbSprintsRealises;

    //@DBRef
    //private Set<Developer> developers = new HashSet<>();

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

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

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<String> developers) {
        this.developers = developers;
    }

    //public int getNbSprints() {return nbSprints;}

    //public void setNbSprints(int nbSprints) {this.nbSprints = nbSprints;}

    //public int getNbSprintsRealises() {return nbSprintsRealises;}

    //public void setNbSprintsRealises(int nbSprintsRealises) {this.nbSprintsRealises = nbSprintsRealises;}

    public ProjectDTO(String id, String title, String description, Date startdate, Date enddate, String status, String creator, List<String> developers) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
        this.status = status;
        this.creator = creator;
        this.developers = developers;
        //this.nbSprints = nbSprints;
        //this.nbSprintsRealises = nbSprintsRealises;
    }

    public ProjectDTO() {
    }
}
