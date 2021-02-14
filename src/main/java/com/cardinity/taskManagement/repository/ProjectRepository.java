package com.cardinity.taskManagement.repository;

import com.cardinity.taskManagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Override
    Optional<Project> findById(Long projectId);

    Optional<Project> findByIdAndCreatedBy(Long projectId, Long userId);

    List<Project> findByCreatedBy(Long userId);

    List<Project> findProjectByNameContainsAndCreatedBy(String nameQuery, Long userId);
}
