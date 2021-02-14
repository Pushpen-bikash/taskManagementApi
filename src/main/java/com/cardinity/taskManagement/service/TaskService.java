package com.cardinity.taskManagement.service;

import com.cardinity.taskManagement.entity.*;
import com.cardinity.taskManagement.exception.AppException;
import com.cardinity.taskManagement.exception.BadRequestException;
import com.cardinity.taskManagement.exception.ResourceNotFoundException;
import com.cardinity.taskManagement.model.TaskEditRequest;
import com.cardinity.taskManagement.model.TaskRequest;
import com.cardinity.taskManagement.model.TaskResponse;
import com.cardinity.taskManagement.repository.*;
import com.cardinity.taskManagement.security.UserPrincipal;
import com.cardinity.taskManagement.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

   private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Transactional
    public Task save(TaskRequest taskRequest, UserPrincipal currentUser) {
        Task task = new Task();
        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        User usr = userRepository.findById(currentUser.getId()).orElseThrow(()->
                new ResourceNotFoundException("User", "userId", currentUser.getId()));
        setUser(usr);
        setTaskProjectAndStatus(taskRequest.getProjectId(), taskRequest.getStatusId(), task);
        return taskRepository.save(task);
    }

    @Transactional
    public Task edit(TaskEditRequest taskEditRequest, UserPrincipal currentUser) {
        Task task = taskRepository.findByIdAndCreatedBy(taskEditRequest.getId(), currentUser.getId()).orElseThrow(() ->
        new BadRequestException("Task is not found with taskId: "+taskEditRequest.getId()+" and userId: "+currentUser.getId()+""));
        task.setDescription(taskEditRequest.getDescription());
        setTaskProjectAndStatus(task.getProject().getId(), taskEditRequest.getStatusId(), task);
        if(task.getStatus().getStatus().name().equals("Closed")){
            throw new BadRequestException("Closed task can not be modified");
        }
        task.setDueDate(taskEditRequest.getDueDate());
        return taskRepository.save(task);
    }

    @Transactional
    public void setTaskProjectAndStatus(Long projectId, Long statusId, Task task) {
        Project project = null;
        if (getUser() != null) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() ->
                    new ResourceNotFoundException("Role", "role", RoleName.ROLE_USER));
            if(getUser().getRoles().contains(userRole)){
                project = projectRepository.findByIdAndCreatedBy(projectId, getUser().getId()).orElseThrow(() ->
                        new BadRequestException("Project is not found with projectId: "+projectId+" and userId: "+getUser().getId()+""));
            }
        } else {
            project = projectRepository.findById(projectId).orElseThrow(() ->
                    new ResourceNotFoundException("Project", "projectId", projectId));
        }
        TaskStatus taskStatus = taskStatusRepository.findById(statusId).orElseThrow(() ->
                new ResourceNotFoundException("TaskStatus", "taskStatusId", statusId));
        task.setStatus(taskStatus);
        task.setProject(project);
        setUser(null);
    }

    @Transactional
    public TaskResponse getTaskByIdAndUser(Long taskId, UserPrincipal currentUser) {
        Task task = taskRepository.findByIdAndCreatedBy(taskId, currentUser.getId()).orElseThrow(() ->
                new BadRequestException("Task is not found with taskId: "+taskId+" and userId: "+getUser().getId()+""));
        setTaskProjectAndStatus(task.getProject().getId(), task.getStatus().getId(), task);
        return ModelMapper.buildTaskResponse(task);
    }

    @Transactional
    public List<TaskResponse> getTaskByProjectAndUser(Long projectId, UserPrincipal currentUser) {
        List<Task> taskList = taskRepository.findByProjectIdAndCreatedBy(projectId, currentUser.getId());
        return buildAndTaskResponseList(taskList);
    }

    @Transactional
    public List<TaskResponse> getTaskByStatusAndUser(Long statusId, UserPrincipal currentUser) {
        List<Task> taskList = taskRepository.findByStatusIdAndCreatedBy(statusId, currentUser.getId());
        return buildAndTaskResponseList(taskList);
    }

    @Transactional
    public List<TaskResponse> getExpiredTask(UserPrincipal currentUser) {
        List<Task> taskList = taskRepository.findExpiredTask(new Date(), currentUser.getId());
        return buildAndTaskResponseList(taskList);
    }

    @Transactional
    public List<TaskResponse> getTaskByUser(Long userId) {
        List<Task> taskList = taskRepository.findByCreatedBy(userId);
        return buildAndTaskResponseList(taskList);
    }

    public List<TaskResponse> buildAndTaskResponseList(List<Task> taskList) {
        List<TaskResponse> taskResponseList = new ArrayList<>();
        for (Task task : taskList) {
            setTaskProjectAndStatus(task.getProject().getId(), task.getStatus().getId(), task);
            taskResponseList.add(ModelMapper.buildTaskResponse(task));
        }
        return taskResponseList;
    }

    @Transactional
    public List<TaskResponse> getTaskByNameAndUser(String name, UserPrincipal currentUser) {
        List<Task> taskList = taskRepository.findTaskByNameContainsAndCreatedBy(name, currentUser.getId());
        return buildAndTaskResponseList(taskList);
    }
}
