package com.anush.aiproject.dto.request;

import com.anush.aiproject.shared.constants.ProjectStatus;

import lombok.Data;

@Data
public class ProjectFilterRequest {

    private String search;
    private ProjectStatus status;
 
}
