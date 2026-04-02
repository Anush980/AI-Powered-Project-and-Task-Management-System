package com.anush.aiproject.service;

import com.anush.aiproject.dto.request.AuthRequest;
import com.anush.aiproject.dto.response.AuthResponse;

public interface AuthService {

    void register(AuthRequest request);

    AuthResponse login(AuthRequest request);

}
