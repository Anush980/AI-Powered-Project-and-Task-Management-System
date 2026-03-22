package com.anush.aiproject.dto.request;

import lombok.Data;

@Data
public class ProfileRequest {
     
    private String fullName;

    private String address;

    private String phone;

    private String profileImgUrl;
}
