package com.anush.aiproject.service;

import java.util.List;

import com.anush.aiproject.dto.request.TaskRequest;
import com.anush.aiproject.dto.response.TaskResponse;
import com.anush.aiproject.entity.User;

public interface TaskService {
    
    List<TaskResponse> getTasksByProject(User currentUser, Long projectId);
    
    TaskResponse getTaskById(User currentUser, Long taskId);
    
    TaskResponse createTask(User currentUser, Long projectId, TaskRequest request);
    
    TaskResponse updateTask(User currentUser, Long taskId, TaskRequest request);
    
    void deleteTask(User currentUser, Long taskId);
    
    TaskResponse updateTaskStatus(User currentUser, Long taskId, String status);
}
