package com.anush.aiproject.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anush.aiproject.dto.request.ProjectRequest;
import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.ProjectResponse;
import com.anush.aiproject.entity.User;

public interface ProjectService {
    
    PageResponse<ProjectResponse> getProjects(User currentUser,Pageable pageable);

    ProjectResponse getProjectById(User currentUser, Long projectId);

    ProjectResponse addProject(User currentUser, ProjectRequest request);

    ProjectResponse updateProject(User currentUser, Long projectId, ProjectRequest request);

    void deleteProject(User currentUser, Long projectId);
}

