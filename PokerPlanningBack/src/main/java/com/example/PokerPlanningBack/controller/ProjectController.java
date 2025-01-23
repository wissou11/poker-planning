package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.model.ProjectDTO;
import com.example.PokerPlanningBack.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.PokerPlanningBack.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping("/upload-projects-data")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file){
        if(ExcelUploadService.isValidExcelFile(file)){
            try {
                List<ProjectDTO> projects = ExcelUploadService.getProjectsDataFromExcel(file.getInputStream());
                this.projectRepository.saveAll(projects);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
        return ResponseEntity
                .ok(Map.of("Message" , " Projects data uploaded and saved to database successfully"));
    }

    @CrossOrigin
    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjects(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ProjectDTO> projectPage = projectRepository.findAll(pageable);

        if (projectPage.hasContent()) {
            List<ProjectDTO> projects = projectPage.getContent();
            for (ProjectDTO project : projects) {
                projectRepository.save(updateProjectStatus(project));
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No projects available", HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping("/projects/dev/{devid}")
    public ResponseEntity<?> getAllDeveloperProjects(@RequestParam(value = "page", defaultValue = "0") int page, @PathVariable("devid") String devid) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ProjectDTO> projectPage = projectRepository.findAll(pageable);

        if (projectPage.hasContent()) {
            // Filtrer les projets pour ceux contenant le `devid` dans la liste des développeurs
            List<ProjectDTO> filteredProjects = projectPage.getContent()
                    .stream()
                    .filter(project -> project.getDevelopers() != null && project.getDevelopers().contains(devid))
                    .toList();

            if (!filteredProjects.isEmpty()) {
                return new ResponseEntity<>(filteredProjects, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No projects found for the specified developer ID", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("No projects available", HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping("/projects")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO project) {
        try {
            project = updateProjectStatus(project);
            projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @GetMapping("/projects/{id}")
    public ResponseEntity<?> getSingleProject(@PathVariable("id") String id) {
        Optional<ProjectDTO> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            return new ResponseEntity<>(projectOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Project not found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PutMapping("/projects/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody ProjectDTO projectDTO) {
        Optional<ProjectDTO> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            ProjectDTO projectToSave = projectOptional.get();
            projectToSave.setStatus(projectDTO.getStatus() != null ? projectDTO.getStatus() : projectToSave.getStatus());
            projectToSave.setTitle(projectDTO.getTitle() != null ? projectDTO.getTitle() : projectToSave.getTitle());
            projectToSave.setDescription(projectDTO.getDescription() != null ? projectDTO.getDescription() : projectToSave.getDescription());
            projectToSave.setCreator(projectDTO.getCreator() != null ? projectDTO.getCreator() : projectToSave.getCreator());
            projectToSave.setStartdate(projectDTO.getStartdate() != null ? projectDTO.getStartdate() : projectToSave.getStartdate());
            projectToSave.setEnddate(projectDTO.getEnddate() != null ? projectDTO.getEnddate() : projectToSave.getEnddate());
            projectRepository.save(updateProjectStatus(projectToSave));
            return new ResponseEntity<>(projectToSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Project not found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        try {
            projectRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted project with id " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private ProjectDTO updateProjectStatus(ProjectDTO project) {
        Date currentDate = new Date();
        if (project.getStartdate().after(currentDate)) {
            project.setStatus("Planned");
        } else if (project.getStartdate().before(currentDate) && project.getEnddate().after(currentDate)) {
            project.setStatus("In Progress");
        } else if (project.getEnddate().before(currentDate) || project.getEnddate().equals(currentDate)) {
            project.setStatus("Done");
        }
        return project;
    }
}
