package com.anush.aiproject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anush.aiproject.dto.request.SubtaskFilterRequest;
import com.anush.aiproject.dto.request.SubtaskRequest;
import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.SubtaskResponse;
import com.anush.aiproject.entity.Subtask;
import com.anush.aiproject.entity.Task;
import com.anush.aiproject.entity.User;
import com.anush.aiproject.repository.SubtaskRepository;
import com.anush.aiproject.repository.TaskRepository;
import com.anush.aiproject.repository.UserRepository;
import com.anush.aiproject.service.SubtaskService;
import com.anush.aiproject.shared.constants.TaskStatus;
import com.anush.aiproject.shared.exception.ResourceNotFoundException;
import com.anush.aiproject.specification.SubtaskSpecification;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SubtaskResponse> getSubtasksByTask(User currentUser, Long taskId, SubtaskFilterRequest filter, @NotNull Pageable pageable) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task belongs to user's project
        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task not found or access denied");
        }

        var spec = SubtaskSpecification.build(taskId, filter);
        Page<Subtask> page = subtaskRepository.findAll(spec, pageable);
        
        return PageResponse.<SubtaskResponse>builder()
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
    public SubtaskResponse getSubtaskById(User currentUser, Long subtaskId) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found"));

        // Verify subtask belongs to user's project
        if (!subtask.getTask().getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Subtask not found or access denied");
        }

        return toResponse(subtask);
    }

    @Override
    @Transactional
    public SubtaskResponse createSubtask(User currentUser, Long taskId, SubtaskRequest request) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task belongs to user's project
        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task not found or access denied");
        }

        Subtask subtask = new Subtask();
        subtask.setTitle(request.getTitle());
        subtask.setDescription(request.getDescription());
        subtask.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        subtask.setTask(task);

        Subtask savedSubtask = subtaskRepository.save(subtask);
        return toResponse(savedSubtask);
    }

    @Override
    @Transactional
    public SubtaskResponse updateSubtask(User currentUser, Long subtaskId, SubtaskRequest request) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found"));

        // Verify subtask belongs to user's project
        if (!subtask.getTask().getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Subtask not found or access denied");
        }

        subtask.setTitle(request.getTitle());
        subtask.setDescription(request.getDescription());
        subtask.setStatus(request.getStatus());

        Subtask updatedSubtask = subtaskRepository.save(subtask);
        return toResponse(updatedSubtask);
    }

    @Override
    @Transactional
    public void deleteSubtask(User currentUser, Long subtaskId) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found"));

        // Verify subtask belongs to user's project
        if (!subtask.getTask().getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Subtask not found or access denied");
        }

        subtaskRepository.delete(subtask);
    }

    // Helper method
    private SubtaskResponse toResponse(Subtask subtask) {
        return SubtaskResponse.builder()
                .id(subtask.getId())
                .title(subtask.getTitle())
                .description(subtask.getDescription())
                .status(subtask.getStatus())
                .taskId(subtask.getTask().getId())
                .createdAt(subtask.getCreatedAt())
                .updatedAt(subtask.getUpdatedAt())
                .build();
    }
}
