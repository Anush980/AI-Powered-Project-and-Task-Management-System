package com.anush.aiproject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anush.aiproject.dto.request.TaskRequest;
import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.TaskResponse;
import com.anush.aiproject.entity.Project;
import com.anush.aiproject.entity.Task;
import com.anush.aiproject.entity.User;
import com.anush.aiproject.repository.ProjectRepository;
import com.anush.aiproject.repository.TaskRepository;
import com.anush.aiproject.repository.UserRepository;
import com.anush.aiproject.service.TaskService;
import com.anush.aiproject.shared.constants.TaskStatus;
import com.anush.aiproject.shared.exception.BadRequestException;
import com.anush.aiproject.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getTasksByProject(User currentUser, Long projectId,Pageable pageable) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // Verify project belongs to user
                projectRepository.findById(projectId)
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or access denied"));

        Page<Task> page = taskRepository.findAllByProjectId(projectId,pageable);
        return PageResponse.<TaskResponse>builder()
                                .content(page.getContent().stream().map(this::toResponse).toList())
                                .page(page.getNumber())
                                .size(page.getSize())
                                .totalElements(page.getTotalElements())
                                .totalPages(page.getTotalPages())
                                .hasNext(page.hasNext())
                                .hasPrevious(page.hasPrevious())
                                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(User currentUser, Long taskId) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task belongs to user's project
        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task not found or access denied");
        }

        return toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse createTask(User currentUser, Long projectId, TaskRequest request) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = projectRepository.findById(projectId)
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or access denied"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setPriority(request.getPriority());
        task.setDeadline(request.getDeadline());
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        return toResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(User currentUser, Long taskId, TaskRequest request) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task belongs to user's project
        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task not found or access denied");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDeadline(request.getDeadline());

        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(User currentUser, Long taskId) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task belongs to user's project
        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task not found or access denied");
        }

        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(User currentUser, Long taskId, String status) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task belongs to user's project
        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task not found or access denied");
        }

        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            task.setStatus(taskStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid task status: " + status);
        }

        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }

    // Helper method
    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .deadline(task.getDeadline())
                .projectId(task.getProject().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
