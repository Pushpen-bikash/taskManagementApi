package com.cardinity.taskManagement.controller;

import com.cardinity.taskManagement.entity.Project;
import com.cardinity.taskManagement.model.ApiResponse;
import com.cardinity.taskManagement.model.ProjectRequest;
import com.cardinity.taskManagement.model.ProjectResponse;
import com.cardinity.taskManagement.security.CurrentUser;
import com.cardinity.taskManagement.security.UserPrincipal;
import com.cardinity.taskManagement.service.ProjectService;
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
@RequestMapping("api/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest){
        Project result = projectService.save(projectRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Project created successfully"));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/delete/{projectId}")
    ResponseEntity<?> deleteProject(@PathVariable(value = "projectId") Long projectId, @CurrentUser UserPrincipal currentUser){
        projectService.delete(projectId, currentUser);
        return new ResponseEntity<>(new ApiResponse(true, "Project deleted successfully"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/all")
    List<ProjectResponse> getAllProject(@CurrentUser UserPrincipal currentUser){
        return projectService.getProjectByCurrentUser(currentUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/id/{projectId}")
    ProjectResponse getProjectById(@PathVariable(value = "projectId") Long projectId, @CurrentUser UserPrincipal currentUser) {
        return projectService.getProjectByIdAndUser(projectId, currentUser);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/name/{projectName}")
    List<ProjectResponse> getProjectByName(@PathVariable(value = "projectName") String projectName, @CurrentUser UserPrincipal currentUser) {
        return projectService.getProjectByNameAndUser(projectName, currentUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    List<ProjectResponse> getProjectByUser(@PathVariable(value = "userId") Long userId){
        return projectService.getProjectByUser(userId);
    }

}
