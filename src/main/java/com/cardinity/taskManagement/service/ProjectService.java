package com.cardinity.taskManagement.service;

import com.cardinity.taskManagement.entity.Project;
import com.cardinity.taskManagement.exception.AppException;
import com.cardinity.taskManagement.exception.ResourceNotFoundException;
import com.cardinity.taskManagement.model.ProjectRequest;
import com.cardinity.taskManagement.model.ProjectResponse;
import com.cardinity.taskManagement.repository.ProjectRepository;
import com.cardinity.taskManagement.security.UserPrincipal;
import com.cardinity.taskManagement.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Transactional
    public Project save(ProjectRequest projectRequest) {
        Project project = new Project();
        project.setName(projectRequest.getName());
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long projectId, UserPrincipal currentUser) {
        Project project = projectRepository.findByIdAndCreatedBy(projectId, currentUser.getId()).orElseThrow(() -> new AppException("Project is not found"));
        projectRepository.delete(project);
    }

    @Transactional
    public List<ProjectResponse> getProjectByCurrentUser(UserPrincipal currentUser) {
        List<Project> projectList = projectRepository.findByCreatedBy(currentUser.getId());
        return ModelMapper.buildProjectResponseList(projectList);
    }

    @Transactional
    public List<ProjectResponse> getProjectByUser(Long userId) {
        List<Project> projectList = projectRepository.findByCreatedBy(userId);
        return ModelMapper.buildProjectResponseList(projectList);
    }

    @Transactional
    public List<ProjectResponse> getProjectByNameAndUser(String name, UserPrincipal currentUser){
        List<Project> projectList = projectRepository.findProjectByNameContainsAndCreatedBy(name, currentUser.getId());
        return ModelMapper.buildProjectResponseList(projectList);
    }

    @Transactional
    public ProjectResponse getProjectByIdAndUser(Long projectId, UserPrincipal currentUser){
        Project project = projectRepository.findByIdAndCreatedBy(projectId, currentUser.getId()).orElseThrow(()->
                new ResourceNotFoundException("Project", "ProjectId", projectId));
        return ModelMapper.buildProjectResponse(project);
    }
}
