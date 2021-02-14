package com.cardinity.taskManagement.util;

import com.cardinity.taskManagement.entity.Project;
import com.cardinity.taskManagement.entity.Task;
import com.cardinity.taskManagement.model.ProjectResponse;
import com.cardinity.taskManagement.model.TaskResponse;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {

    public static ProjectResponse buildProjectResponse(Project project){
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setName(project.getName());
        return projectResponse;
    }

    public static List<ProjectResponse> buildProjectResponseList(List<Project> projectList){
        List<ProjectResponse> projectResponseList = new ArrayList<>();
        for (Project project: projectList){
            projectResponseList.add(buildProjectResponse(project));
        }
        return projectResponseList;
    }

    public static TaskResponse buildTaskResponse(Task task){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setName(task.getName());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setDueDate(task.getDueDate());
        taskResponse.setProject(buildProjectResponse(task.getProject()));
        taskResponse.setStatus(task.getStatus().getStatus().name());
        return taskResponse;
    }


}
