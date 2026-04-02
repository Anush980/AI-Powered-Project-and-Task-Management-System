package com.anush.aiproject.dto.response;

import java.time.LocalDateTime;

import com.anush.aiproject.shared.constants.TaskPriority;
import com.anush.aiproject.shared.constants.TaskStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDateTime deadline;

    private Long projectId;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
