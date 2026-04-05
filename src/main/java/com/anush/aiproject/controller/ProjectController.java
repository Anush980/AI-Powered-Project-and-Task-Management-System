package com.anush.aiproject.controller;

import com.anush.aiproject.dto.request.PaginationRequest;
import com.anush.aiproject.dto.request.ProjectRequest;
import com.anush.aiproject.dto.response.ApiResponse;
import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.ProjectResponse;
import com.anush.aiproject.service.ProjectService;
import com.anush.aiproject.shared.constants.ApiPath;
import com.anush.aiproject.shared.util.PaginationUtil;
import com.anush.aiproject.shared.util.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(ApiPath.PROJECT)
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * GET /v1/projects - Get all projects for current user
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProjectResponse>>> getAllProjects(@ModelAttribute PaginationRequest paginationRequest) {
        var currentUser = SecurityUtils.getCurrentUser();
        Pageable pageable = PaginationUtil.of(paginationRequest);
        PageResponse<ProjectResponse> projects = projectService.getProjects(currentUser, pageable);
        return ResponseEntity.ok(ApiResponse.success(projects, "Projects retrieved successfully"));
    }

    /**
     * GET /v1/projects/{id} - Get project by ID
     */
    @GetMapping(ApiPath.ID)
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        var currentUser = SecurityUtils.getCurrentUser();
        ProjectResponse project = projectService.getProjectById(currentUser, id);
        return ResponseEntity.ok(
            ApiResponse.success(project, "Project retrieved successfully")
        );
    }

    /**
     * POST /v1/projects - Create new project
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody ProjectRequest request) {
        var currentUser = SecurityUtils.getCurrentUser();
        ProjectResponse project = projectService.addProject(currentUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(project, "Project created successfully")
        );
    }

    /**
     * PUT /v1/projects/{id} - Update project
     */
    @PutMapping(ApiPath.ID)
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        var currentUser = SecurityUtils.getCurrentUser();
        ProjectResponse project = projectService.updateProject(currentUser, id, request);
        return ResponseEntity.ok(
            ApiResponse.success(project, "Project updated successfully")
        );
    }

    /**
     * DELETE /v1/projects/{id} - Delete project
     */
    @DeleteMapping(ApiPath.ID)
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        var currentUser = SecurityUtils.getCurrentUser();
        projectService.deleteProject(currentUser, id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Project deleted successfully")
        );
    }
}
