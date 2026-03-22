package com.anush.aiproject.dto.response;

import java.time.LocalDateTime;

import com.anush.aiproject.shared.constants.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private Long id;

    private String token;

    private String email;

    private UserRole role;

    private Boolean suspended;

    private LocalDateTime createdAt;

}
