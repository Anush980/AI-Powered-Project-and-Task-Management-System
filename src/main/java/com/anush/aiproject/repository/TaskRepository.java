package com.anush.aiproject.repository;

import java.util.List;

import com.anush.aiproject.entity.Task;

public interface TaskRepository extends AbstractRepository<Task,Long> {

        List<Task> findByProjectId(Long projectId);

        List<Task> findAllByProjectId(Long projectId);
} 
