package com.anush.aiproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
     
    private String fullName;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    private String address;

    private String phone;

    private String profileImgUrl;
}
