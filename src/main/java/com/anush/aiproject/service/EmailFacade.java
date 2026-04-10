package com.anush.aiproject.service;

public interface EmailFacade {
    
    void sendWelcomeEmail(String to, String name);
    
    void sendResetPasswordEmail(String to, String resetLink);
}
