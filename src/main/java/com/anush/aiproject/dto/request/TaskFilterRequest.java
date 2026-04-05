package com.anush.aiproject.dto.request;

import java.time.LocalDateTime;

import com.anush.aiproject.shared.constants.TaskPriority;
import com.anush.aiproject.shared.constants.TaskStatus;

import lombok.Data;

@Data
public class TaskFilterRequest {

    private String search;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime deadlineBefore;
    private LocalDateTime deadlineAfter;
}
