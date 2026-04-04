package com.anush.aiproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anush.aiproject.dto.request.SubtaskRequest;
import com.anush.aiproject.dto.response.ApiResponse;
import com.anush.aiproject.dto.response.SubtaskResponse;
import com.anush.aiproject.service.SubtaskService;
import com.anush.aiproject.shared.util.SecurityUtils;
import com.anush.aiproject.shared.constants.ApiPath;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPath.SUBTASK)
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskService;

    /**
     * GET /v1/subtasks?taskId={taskId} - Get all subtasks for a task
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubtaskResponse>>> getSubtasksByTask(
            @RequestParam Long taskId) {
        var currentUser = SecurityUtils.getCurrentUser();
        List<SubtaskResponse> subtasks = subtaskService.getSubtasksByTask(currentUser, taskId);
        return ResponseEntity.ok(
            ApiResponse.success(subtasks, "Subtasks retrieved successfully")
        );
    }

    /**
     * GET /v1/subtasks/{id} - Get subtask by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubtaskResponse>> getSubtaskById(@PathVariable Long id) {
        var currentUser = SecurityUtils.getCurrentUser();
        SubtaskResponse subtask = subtaskService.getSubtaskById(currentUser, id);
        return ResponseEntity.ok(
            ApiResponse.success(subtask, "Subtask retrieved successfully")
        );
    }

    /**
     * POST /v1/subtasks?taskId={taskId} - Create new subtask
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SubtaskResponse>> createSubtask(
            @RequestParam Long taskId,
            @Valid @RequestBody SubtaskRequest request) {
        var currentUser = SecurityUtils.getCurrentUser();
        SubtaskResponse subtask = subtaskService.createSubtask(currentUser, taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(subtask, "Subtask created successfully")
        );
    }

    /**
     * PUT /v1/subtasks/{id} - Update subtask
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubtaskResponse>> updateSubtask(
            @PathVariable Long id,
            @Valid @RequestBody SubtaskRequest request) {
        var currentUser = SecurityUtils.getCurrentUser();
        SubtaskResponse subtask = subtaskService.updateSubtask(currentUser, id, request);
        return ResponseEntity.ok(
            ApiResponse.success(subtask, "Subtask updated successfully")
        );
    }

    /**
     * DELETE /v1/subtasks/{id} - Delete subtask
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubtask(@PathVariable Long id) {
        var currentUser = SecurityUtils.getCurrentUser();
        subtaskService.deleteSubtask(currentUser, id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Subtask deleted successfully")
        );
    }
}
