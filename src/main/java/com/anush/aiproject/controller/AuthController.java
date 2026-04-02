package com.anush.aiproject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anush.aiproject.dto.request.AuthRequest;
import com.anush.aiproject.dto.response.ApiResponse;
import com.anush.aiproject.dto.response.AuthResponse;
import com.anush.aiproject.service.AuthService;
import com.anush.aiproject.shared.constants.ApiPath;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.AUTH)
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiPath.AUTH_REGISTER)
public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody @Valid AuthRequest request){

    authService.register(request);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(null, "Account Created successfully. please login"));

}

    @PostMapping(ApiPath.AUTH_LOGIN)
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(response, "Login successful"));
    }

}
