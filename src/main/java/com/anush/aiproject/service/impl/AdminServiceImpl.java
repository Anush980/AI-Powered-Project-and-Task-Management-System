package com.anush.aiproject.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anush.aiproject.dto.response.PageResponse;
import com.anush.aiproject.dto.response.UserResponse;
import com.anush.aiproject.entity.User;
import com.anush.aiproject.repository.ProjectRepository;
import com.anush.aiproject.repository.TaskRepository;
import com.anush.aiproject.repository.UserRepository;
import com.anush.aiproject.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    

// public PageResponse<UserResponse> getAllUsers(User currentUser, Pageable pageable){

                    

}
