package com.anush.aiproject.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anush.aiproject.dto.request.PaginationRequest;
import com.anush.aiproject.dto.request.TaskFilterRequest;
import com.anush.aiproject.dto.request.TaskRequest;
import com.anush.aiproject.dto.response.ApiResponse;
import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.TaskResponse;
import com.anush.aiproject.service.TaskService;
import com.anush.aiproject.shared.constants.ApiPath;
import com.anush.aiproject.shared.util.PaginationUtil;
import com.anush.aiproject.shared.util.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPath.TASK)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * GET /v1/tasks?projectId={projectId} - Get all tasks for a project
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTasksByProject(
            @RequestParam Long projectId, 
                @ModelAttribute TaskFilterRequest filter,
            @ModelAttribute PaginationRequest paginationRequest) {
        var currentUser = SecurityUtils.getCurrentUser();
        Pageable pageable = PaginationUtil.of(paginationRequest);
        PageResponse<TaskResponse> tasks = taskService.getTasksByProject(currentUser, projectId, filter, pageable);
        return ResponseEntity.ok(
            ApiResponse.success(tasks, "Tasks retrieved successfully")
        );
    }

    /**
     * GET /v1/tasks/{id} - Get task by ID
     */
    @GetMapping(ApiPath.ID)
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        var currentUser = SecurityUtils.getCurrentUser();
        TaskResponse task = taskService.getTaskById(currentUser, id);
        return ResponseEntity.ok(
            ApiResponse.success(task, "Task retrieved successfully")
        );
    }

    /**
     * POST /v1/tasks?projectId={projectId} - Create new task
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @RequestParam Long projectId,
            @Valid @RequestBody TaskRequest request) {
        var currentUser = SecurityUtils.getCurrentUser();
        TaskResponse task = taskService.createTask(currentUser, projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(task, "Task created successfully")
        );
    }

    /**
     * PUT /v1/tasks/{id} - Update task
     */
    @PutMapping(ApiPath.ID)
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        var currentUser = SecurityUtils.getCurrentUser();
        TaskResponse task = taskService.updateTask(currentUser, id, request);
        return ResponseEntity.ok(
            ApiResponse.success(task, "Task updated successfully")
        );
    }

    /**
     * PATCH /v1/tasks/{id}/status?status={status} - Update task status only
     */
    @PatchMapping(ApiPath.ID+ApiPath.STATUS)
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        var currentUser = SecurityUtils.getCurrentUser();
        TaskResponse task = taskService.updateTaskStatus(currentUser, id, status);
        return ResponseEntity.ok(
            ApiResponse.success(task, "Task status updated successfully")
        );
    }

    /**
     * DELETE /v1/tasks/{id} - Delete task
     */
    @DeleteMapping(ApiPath.ID)
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        var currentUser = SecurityUtils.getCurrentUser();
        taskService.deleteTask(currentUser, id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Task deleted successfully")
        );
    }
}
