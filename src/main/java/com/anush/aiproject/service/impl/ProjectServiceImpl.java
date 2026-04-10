package com.anush.aiproject.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anush.aiproject.dto.request.ProjectFilterRequest;
import com.anush.aiproject.dto.request.ProjectRequest;
import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.ProjectResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anush.aiproject.entity.Project;
import com.anush.aiproject.entity.User;
import com.anush.aiproject.repository.ProjectRepository;
import com.anush.aiproject.repository.UserRepository;
import com.anush.aiproject.service.ProjectService;
import com.anush.aiproject.shared.constants.ProjectStatus;
import com.anush.aiproject.shared.constants.TaskStatus;
import com.anush.aiproject.shared.exception.ResourceNotFoundException;
import com.anush.aiproject.specification.ProjectSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

        private final UserRepository userRepository;
        private final ProjectRepository projectRepository;

        @Override
        @Transactional(readOnly = true)
        public PageResponse<ProjectResponse> getProjects(User currentUser, ProjectFilterRequest filter,
                        Pageable pageable) {

                Long userId = currentUser.getId();
                if (userId == null)
                        throw new ResourceNotFoundException("User not found");

                if (!userRepository.existsById(userId)) {
                        throw new ResourceNotFoundException("User not found");
                }

                 var spec = ProjectSpecification.build(userId, filter);

                Page<Project> page = projectRepository.findAll(spec, pageable);
                
                return PageResponse.<ProjectResponse>builder()
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
        public ProjectResponse getProjectById(User currentUser, Long projectId) {

                Long userId = currentUser.getId();
                if (userId == null)
                        throw new ResourceNotFoundException("User not found");
                var user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Project project = projectRepository.findById(projectId)
                                .filter(proj -> proj.getUser().getId().equals(user.getId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Project not found or access denied"));
                return toResponse(project);
        }

        @Override
        @Transactional
        public ProjectResponse addProject(User currentUser, ProjectRequest request) {

                Long userId = currentUser.getId();
                if (userId == null)
                        throw new ResourceNotFoundException("User not found");
                var user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Project project = new Project();
                project.setTitle(request.getTitle());
                project.setDescription(request.getDescription());
                project.setStatus(ProjectStatus.PLANNING);
                project.setUser(user);

                Project savedProject = projectRepository.save(project);
                return toResponse(savedProject);
        }

        @Override
        @Transactional
        public ProjectResponse updateProject(User currentUser, Long projectId, ProjectRequest request) {

                Long userId = currentUser.getId();
                if (userId == null)
                        throw new ResourceNotFoundException("User not found");
                var user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Project project = projectRepository.findById(projectId)
                                .filter(proj -> proj.getUser().getId().equals(user.getId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Project not found or access denied"));

                project.setTitle(request.getTitle());
                project.setDescription(request.getDescription());

                Project updatedProject = projectRepository.save(project);
                return toResponse(updatedProject);
        }

        @Override
        @Transactional
        public void deleteProject(User currentUser, Long projectId) {

                Long userId = currentUser.getId();
                if (userId == null)
                        throw new ResourceNotFoundException("User not found");
                var user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Project project = projectRepository.findById(projectId)
                                .filter(proj -> proj.getUser().getId().equals(user.getId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Project not found or access denied"));

                projectRepository.delete(project);
        }

        // Helper method
        private ProjectResponse toResponse(Project project) {
                // Calculate task statistics
                int totalTasks = project.getTasks() != null ? project.getTasks().size() : 0;
                int completedTasks = project.getTasks() != null ? (int) project.getTasks().stream()
                                .filter(task -> task.getStatus() == TaskStatus.DONE)
                                .count() : 0;

                return ProjectResponse.builder()
                                .id(project.getId())
                                .title(project.getTitle())
                                .description(project.getDescription())
                                .status(project.getStatus())
                                .userId(project.getUser().getId())
                                .totalTasks(totalTasks)
                                .completedTasks(completedTasks)
                                .createdAt(project.getCreatedAt())
                                .updatedAt(project.getUpdatedAt())
                                .build();
        }
}
