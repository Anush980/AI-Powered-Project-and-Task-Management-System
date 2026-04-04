package com.anush.aiproject.service;

import java.util.List;

import com.anush.aiproject.dto.request.ProjectRequest;
import com.anush.aiproject.dto.response.ProjectResponse;
import com.anush.aiproject.entity.User;

public interface ProjectService {
    
    List<ProjectResponse> getProjects(User currentUser);

    ProjectResponse getProjectById(User currentUser, Long projectId);

    ProjectResponse addProject(User currentUser, ProjectRequest request);

    ProjectResponse updateProject(User currentUser, Long projectId, ProjectRequest request);

    void deleteProject(User currentUser, Long projectId);
}

