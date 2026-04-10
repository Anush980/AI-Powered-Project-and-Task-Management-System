package com.anush.aiproject.service.impl;


import com.anush.aiproject.service.EmailFacade;
import com.anush.aiproject.service.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailFacadeImpl implements EmailFacade {

    private final EmailService emailService;

    @Override
    public void sendWelcomeEmail(String to, String name) {
        emailService.sendEmail(
                to,
                "Welcome to the App 🎉",
                "welcome",
                Map.of("name", name)
        );
    }

    @Override
    public void sendResetPasswordEmail(String to, String resetLink) {
        emailService.sendEmail(
                to,
                "Reset your password",
                "reset-password",
                Map.of("resetLink", resetLink)
        );
    }
}