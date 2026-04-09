package com.anush.aiproject.service;

import org.springframework.data.domain.Pageable;

import com.anush.aiproject.dto.request.SubtaskRequest;
import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.SubtaskResponse;
import com.anush.aiproject.entity.User;

public interface SubtaskService {
    
    PageResponse<SubtaskResponse> getSubtasksByTask(User currentUser, Long taskId,Pageable pageable);
    
    SubtaskResponse getSubtaskById(User currentUser, Long subtaskId);
    
    SubtaskResponse createSubtask(User currentUser, Long taskId, SubtaskRequest request);
    
    SubtaskResponse updateSubtask(User currentUser, Long subtaskId, SubtaskRequest request);
    
    void deleteSubtask(User currentUser, Long subtaskId);
}
