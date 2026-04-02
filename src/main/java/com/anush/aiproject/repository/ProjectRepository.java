package com.anush.aiproject.repository;

import java.util.List;

import com.anush.aiproject.entity.Project;

public interface ProjectRepository extends AbstractRepository<Project,Long> {

    List<Project> findAllByUserId(Long userId);
    
    List<Project> findByTitleContainingIgnoreCaseAndUserId(String title, Long userId);
    
} 