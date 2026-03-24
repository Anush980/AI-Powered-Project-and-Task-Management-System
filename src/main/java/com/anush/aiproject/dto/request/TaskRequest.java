package com.anush.aiproject.dto.request;

import java.time.LocalDateTime;

import com.anush.aiproject.shared.constants.TaskPriority;
import com.anush.aiproject.shared.constants.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequest {

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDateTime deadline;
    
}
