package com.cardinity.taskManagement.repository;

import com.cardinity.taskManagement.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    @Override
    Optional<TaskStatus> findById(Long aLong);
}
