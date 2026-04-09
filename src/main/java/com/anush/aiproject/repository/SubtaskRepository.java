package com.anush.aiproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anush.aiproject.entity.Subtask;

public interface SubtaskRepository extends AbstractRepository<Subtask,Long> {
    
    Page<Subtask> findByTaskId(Long taskId, Pageable pageable);

    Page<Subtask> findAllByTaskId(Long taskId,Pageable pageable);
}
