package com.anush.aiproject.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anush.aiproject.dto.response.ProjectResponse;
import com.anush.aiproject.entity.Project;
import com.anush.aiproject.entity.User;
import com.anush.aiproject.repository.ProjectRepository;
import com.anush.aiproject.repository.UserRepository;
import com.anush.aiproject.service.ProjectService;
import com.anush.aiproject.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjects(User currentUser) {
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Project> projects = projectRepository.findAllByUserId(user.getId());
        return projects.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(User currentUser, Long projectId){
        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

                Project project =projectRepository.findById(projectId)
                        .filter(proj-> proj.getId().equals(user.getId()))
                        .orElseThrow(()-> new ResourceNotFoundException("Project not found"));
                        return toResponse(project);

    }

    // helper
    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

}
