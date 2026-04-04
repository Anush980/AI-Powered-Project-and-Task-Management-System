package com.anush.aiproject.service;

import java.util.List;

import com.anush.aiproject.dto.request.SubtaskRequest;
import com.anush.aiproject.dto.response.SubtaskResponse;
import com.anush.aiproject.entity.User;

public interface SubtaskService {
    
    List<SubtaskResponse> getSubtasksByTask(User currentUser, Long taskId);
    
    SubtaskResponse getSubtaskById(User currentUser, Long subtaskId);
    
    SubtaskResponse createSubtask(User currentUser, Long taskId, SubtaskRequest request);
    
    SubtaskResponse updateSubtask(User currentUser, Long subtaskId, SubtaskRequest request);
    
    void deleteSubtask(User currentUser, Long subtaskId);
}
