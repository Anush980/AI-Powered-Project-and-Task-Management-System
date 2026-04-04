package com.anush.aiproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anush.aiproject.dto.response.ApiResponse;
import com.anush.aiproject.shared.constants.ApiPath;



@RestController
@RequestMapping(ApiPath.HEALTH)
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<String>> health(){
        return ResponseEntity.ok(ApiResponse.success("OK", "Api is running"));
    }
    
}
