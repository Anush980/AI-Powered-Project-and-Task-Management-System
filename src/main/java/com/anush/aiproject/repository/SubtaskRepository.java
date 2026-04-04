package com.anush.aiproject.repository;

import java.util.List;

import com.anush.aiproject.entity.Subtask;

public interface SubtaskRepository extends AbstractRepository<Subtask,Long> {
    
    List<Subtask> findByTaskId(Long taskId);

    List<Subtask> findAllByTaskId(Long taskId);
}
