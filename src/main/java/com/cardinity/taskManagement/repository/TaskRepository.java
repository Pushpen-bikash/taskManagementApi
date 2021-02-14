package com.cardinity.taskManagement.repository;

import com.cardinity.taskManagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusId(Long statusId);

    List<Task> findByStatusIdAndCreatedBy(Long statusId, Long userId);

    List<Task> findByProjectId(Long projectId);

    List<Task> findByProjectIdAndCreatedBy(Long projectId, Long UserId);

    @Override
    Optional<Task> findById(Long taskId);

    Optional<Task> findByIdAndCreatedBy(Long taskId, Long userId);

    List<Task> findByDueDate(Date dueDate);

    List<Task> findByDueDateAndCreatedBy(Date dueDate, Long userId);

    @Query("SELECT t FROM Task t where t.dueDate < :currentDate and t.createdBy =:userId")
    List<Task> findExpiredTask(@Param("currentDate") Date currentDate, @Param("userId") Long userId);

    List<Task> findByCreatedBy(Long userId);

    List<Task> findTaskByNameContainsAndCreatedBy(String nameQuery, Long userId);
}
