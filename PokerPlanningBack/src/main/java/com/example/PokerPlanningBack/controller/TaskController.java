package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.model.TaskDTO;
import com.example.PokerPlanningBack.repository.TaskRepository;
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
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/upload-tasks-data")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file){
        if(ExcelUploadService.isValidExcelFile(file)){
            try {
                List<TaskDTO> tasks = ExcelUploadService.getTasksDataFromExcel(file.getInputStream());
                this.taskRepository.saveAll(tasks);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
        return ResponseEntity
                .ok(Map.of("Message" , " Tasks data uploaded and saved to database successfully"));
    }
    @CrossOrigin
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<?> getAllTasksByProjectId(@PathVariable("projectId") String projectId){
        List<TaskDTO> tasks= taskRepository.findByProjectId(projectId);
        if(tasks.size()>0){
            return new ResponseEntity<List<TaskDTO>>(tasks, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No tasks available", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/projects/{projectId}/sprints/{sprintId}/tasks")
    public ResponseEntity<?> getAllTasksBySprintId(@PathVariable("sprintId") String sprintId){
       List<TaskDTO> tasks= taskRepository.findBySprintId(sprintId);
        if(tasks.size()>0){
            return new ResponseEntity<List<TaskDTO>>(tasks, HttpStatus.OK);
        }else{
           return new ResponseEntity<>("No tasks available", HttpStatus.NOT_FOUND);
        }
   }
    @CrossOrigin
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<?> createTaskInProject(@RequestBody TaskDTO task, @PathVariable("projectId") String projectId){
        try {
            task.setProjectId(projectId);
            taskRepository.save(task);
            return new ResponseEntity<TaskDTO>(task, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/projects/{projectId}/sprints/{sprintId}/tasks")
    public ResponseEntity<?> createTaskInSprint(@RequestBody TaskDTO task, @PathVariable("projectId") String projectId, @PathVariable("sprintId") String sprintId){
        try {
            task.setProjectId(projectId);
            task.setSprintId(sprintId);
            taskRepository.save(task);
            return new ResponseEntity<TaskDTO>(task, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CrossOrigin
    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> getSingleTaskInProject(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        Optional<TaskDTO> taskOptional = taskRepository.findByProjectIdAndId(projectId,taskId);
        if(taskOptional.isPresent()){
            return new ResponseEntity<>(taskOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Task not found with id "+taskId, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/projects/{projectId}/sprints/{sprintId}/tasks/{taskId}")
    public ResponseEntity<?> getSingleTaskInSprint(@PathVariable("projectId") String projectId, @PathVariable("sprintId") String sprintId, @PathVariable("taskId") String taskId){
        Optional<TaskDTO> taskOptional = taskRepository.findByProjectIdAndSprintIdAndId(projectId,sprintId,taskId);
        if(taskOptional.isPresent()){
            return new ResponseEntity<>(taskOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Task not found with id "+taskId, HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @PutMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> updateTaskByIdInProject(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody TaskDTO taskDTO){
        Optional<TaskDTO> taskOptional = taskRepository.findByProjectIdAndId(projectId,taskId);
        if(taskOptional.isPresent()){
            TaskDTO taskToSave = taskOptional.get();
            taskToSave.setStatus(taskDTO.getStatus()!=null ? taskDTO.getStatus() : taskToSave.getStatus());
            taskToSave.setTitle(taskDTO.getTitle()!=null ? taskDTO.getTitle() : taskToSave.getTitle());
            taskToSave.setSprintId(taskDTO.getSprintId()!=null ? taskDTO.getSprintId() : taskToSave.getSprintId());
            taskToSave.setDuree(taskDTO.getDuree()!=0? taskDTO.getDuree() : taskToSave.getDuree());
            taskToSave.setDescription(taskDTO.getDescription()!=null? taskDTO.getDescription() : taskToSave.getDescription());
            taskRepository.save(taskToSave);
            return new ResponseEntity<>(taskToSave,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Task not found with id "+taskId, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/projects/{projectId}/sprints/{sprintId}/tasks/{taskId}")
    public ResponseEntity<?> updateTaskByIdInSprint(@PathVariable("projectId") String projectId, @PathVariable("sprintId") String sprintId, @PathVariable("taskId") String taskId, @RequestBody TaskDTO taskDTO){
        Optional<TaskDTO> taskOptional = taskRepository.findByProjectIdAndSprintIdAndId(projectId,sprintId,taskId);
        if(taskOptional.isPresent()){
            TaskDTO taskToSave = taskOptional.get();
            taskToSave.setStatus(taskDTO.getStatus()!=null ? taskDTO.getStatus() : taskToSave.getStatus());
            taskToSave.setTitle(taskDTO.getTitle()!=null ? taskDTO.getTitle() : taskToSave.getTitle());
            taskToSave.setSprintId(taskDTO.getSprintId()!=null ? taskDTO.getSprintId() : taskToSave.getSprintId());
            taskToSave.setDuree(taskDTO.getDuree()!=0? taskDTO.getDuree() : taskToSave.getDuree());
            taskToSave.setDescription(taskDTO.getDescription()!=null? taskDTO.getDescription() : taskToSave.getDescription());
            taskRepository.save(taskToSave);
            return new ResponseEntity<>(taskToSave,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Task not found with id "+taskId, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/projects/{projectId}/sprints/{sprintId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTaskByIdFromSprint(@PathVariable("projectId") String projectId, @PathVariable("sprintId") String sprintId, @PathVariable("taskId") String taskId){
        try {
            taskRepository.deleteByProjectIdAndSprintIdAndId(projectId,sprintId,taskId);
            return new ResponseEntity<>("Successfully deleted task with id "+taskId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTaskByIdFromProject(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        try {
            taskRepository.deleteByProjectIdAndId(projectId,taskId);
            return new ResponseEntity<>("Successfully deleted task with id "+taskId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
