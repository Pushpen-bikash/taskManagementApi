package com.cardinity.taskManagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TaskEditRequest {
    private Long id;
    private String description;
    Long statusId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date dueDate;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}
