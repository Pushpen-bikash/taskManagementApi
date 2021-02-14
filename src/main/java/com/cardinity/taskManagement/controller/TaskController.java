package com.cardinity.taskManagement.controller;

import com.cardinity.taskManagement.entity.Task;
import com.cardinity.taskManagement.model.ApiResponse;
import com.cardinity.taskManagement.model.TaskEditRequest;
import com.cardinity.taskManagement.model.TaskRequest;
import com.cardinity.taskManagement.model.TaskResponse;
import com.cardinity.taskManagement.security.CurrentUser;
import com.cardinity.taskManagement.security.UserPrincipal;
import com.cardinity.taskManagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest, @CurrentUser UserPrincipal currentUser){
        Task result = taskService.save(taskRequest, currentUser);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Task created successfully"));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/editTask")
    ResponseEntity<?> editTask(@Valid @RequestBody TaskEditRequest taskEditRequest, @CurrentUser UserPrincipal currentUser){
        Task result = taskService.edit(taskEditRequest, currentUser);
        return new ResponseEntity<>(new ApiResponse(true, "Task "+result.getName()+ "edited successfully"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/id/{taskId}")
    TaskResponse getTask(@PathVariable(value = "taskId") Long taskId, @CurrentUser UserPrincipal currentUser){
        return taskService.getTaskByIdAndUser(taskId, currentUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/project/{projectId}")
    List<TaskResponse> getTaskByProject(@PathVariable(value = "projectId") Long projectId, @CurrentUser UserPrincipal currentUser){
        return taskService.getTaskByProjectAndUser(projectId, currentUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/status/{statusId}")
    List<TaskResponse> getTaskByStatus(@PathVariable(value = "statusId") Long statusId, @CurrentUser UserPrincipal currentUser){
        return taskService.getTaskByStatusAndUser(statusId, currentUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/expiredTask")
    List<TaskResponse> getExpiredTask(@CurrentUser UserPrincipal currentUser){
        return taskService.getExpiredTask(currentUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/name/{taskName}")
    List<TaskResponse> getTaskByName(@PathVariable(value = "taskName") String taskName, @CurrentUser UserPrincipal currentUser) {
        return taskService.getTaskByNameAndUser(taskName, currentUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    List<TaskResponse> getTaskByUser(@PathVariable(value = "userId") Long userId){
        return taskService.getTaskByUser(userId);
    }
}
