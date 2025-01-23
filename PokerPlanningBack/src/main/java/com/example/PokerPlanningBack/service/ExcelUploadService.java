package com.example.PokerPlanningBack.service;

import com.example.PokerPlanningBack.model.ProjectDTO;
import com.example.PokerPlanningBack.model.TaskDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
    public static List<ProjectDTO> getProjectsDataFromExcel(InputStream inputStream){
        List<ProjectDTO> projects = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("projects");
            int rowIndex =0;
            for (Row row : sheet){
                if (rowIndex ==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                ProjectDTO project = new ProjectDTO();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 -> project.setCreator(cell.getStringCellValue());
                        case 1 -> project.setTitle(cell.getStringCellValue());
                        case 2 -> project.setDescription(cell.getStringCellValue());
                        case 3 -> project.setStartdate(cell.getDateCellValue());
                        case 4 -> project.setEnddate(cell.getDateCellValue());
                        case 5 -> project.setStatus(cell.getStringCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                projects.add(project);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return projects;
    }
    public static List<TaskDTO> getTasksDataFromExcel(InputStream inputStream){
        List<TaskDTO> tasks = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("tasks");
            int rowIndex =0;
            for (Row row : sheet){
                if (rowIndex ==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                TaskDTO task = new TaskDTO();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 -> task.setTitle(cell.getStringCellValue());
                        case 1 -> task.setDescription(cell.getStringCellValue());
                        case 2 -> task.setProjectId(cell.getStringCellValue());
                        case 3 -> task.setStatus(cell.getStringCellValue());
                        case 4 -> task.setDuree((int) cell.getNumericCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                tasks.add(task);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return tasks;
    }

}
