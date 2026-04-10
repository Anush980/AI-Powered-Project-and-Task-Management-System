package com.anush.aiproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anush.aiproject.dto.response.ApiResponse;
import com.anush.aiproject.service.EmailFacade;
import com.anush.aiproject.shared.constants.ApiPath;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping(ApiPath.HEALTH)
@RequiredArgsConstructor
public class HealthController {

    private final EmailFacade emailFacade;

    @GetMapping
    public ResponseEntity<ApiResponse<String>> health(){
        return ResponseEntity.ok(ApiResponse.success("OK", "Api is running"));
    }

    @GetMapping("/email-test")
    public ResponseEntity<ApiResponse<String>> testEmail(@RequestParam String to){
        // Send a welcome email for testing
        emailFacade.sendWelcomeEmail(to, "Anush");
        
        return ResponseEntity.ok(ApiResponse.success(
                "OK",
                "Test email sent to " + to
        ));
    }
    
}
