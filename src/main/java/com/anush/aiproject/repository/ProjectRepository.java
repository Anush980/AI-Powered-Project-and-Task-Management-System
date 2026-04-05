package com.anush.aiproject.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anush.aiproject.entity.Project;

public interface ProjectRepository extends AbstractRepository<Project,Long> {

    Page<Project> findAllByUserId(Long userId, Pageable pageable);

    Page<Project> findByTitleContainingIgnoreCaseAndUserId(String title, Long userId, Pageable pageable);

} 