package com.anush.aiproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "email is required")
    @Email(message = "Valid Email address is required")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 to 50 characters")
    private String password;

}
