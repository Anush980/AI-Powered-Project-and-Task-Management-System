package com.anush.aiproject.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.anush.aiproject.shared.constants.TaskPriority;
import com.anush.aiproject.shared.constants.TaskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class Task extends BaseEntity{

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @OneToMany(mappedBy = "task")
    private List<Subtask> subtasks;
    
}
