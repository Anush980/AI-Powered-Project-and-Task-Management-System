package com.anush.aiproject.shared.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.anush.aiproject.entity.User;
import com.anush.aiproject.shared.exception.BadRequestException;

public class SecurityUtils {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof User) {
            return (User) principal;
        }
        
        throw new BadRequestException("Invalid user authentication");
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
