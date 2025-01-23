package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.ProjectDTO;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface ProjectRepository extends MongoRepository<ProjectDTO,String> {
}
