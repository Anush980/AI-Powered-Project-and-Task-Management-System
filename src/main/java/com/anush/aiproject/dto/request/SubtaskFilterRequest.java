package com.anush.aiproject.dto.request;

import com.anush.aiproject.shared.constants.TaskStatus;

import lombok.Data;

@Data
public class SubtaskFilterRequest {
    
    private String search;

    private TaskStatus status;
}
