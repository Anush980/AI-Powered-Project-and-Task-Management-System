package com.anush.aiproject.service.impl;

import com.anush.aiproject.repository.PasswordHistoryRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anush.aiproject.dto.request.AuthRequest;
import com.anush.aiproject.dto.response.AuthResponse;
import com.anush.aiproject.entity.PasswordHistory;
import com.anush.aiproject.entity.User;
import com.anush.aiproject.repository.UserRepository;
import com.anush.aiproject.security.filter.JwtService;
import com.anush.aiproject.service.AuthService;
import com.anush.aiproject.shared.constants.UserRole;
import com.anush.aiproject.shared.exception.RequestValidationException;
import com.anush.aiproject.shared.exception.ResourceNotFoundException;
import com.anush.aiproject.shared.util.StringUtils;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordHistoryRepository passwordHistoryRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void register(AuthRequest request){

        String email = StringUtils.trim(request.getEmail().toLowerCase());

        if(userRepository.existsByEmail(email)){
            throw new ValidationException("User already exists with this email");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        user.setEmailVerified(false);
        user.setSuspended(false);

        savePasswordHistory(user, user.getPassword());

        userRepository.save(user);

    }

    public AuthResponse login(AuthRequest request){
                User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

                if(Boolean.TRUE.equals(user.getSuspended())){
                    throw new RequestValidationException("Account Suspended. please contact admin");
                }
                if(Boolean.FALSE.equals(user.getEmailVerified())){
                    throw new RequestValidationException("Email not verififed.please check you mail");
                }
                try{
                    authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                }
                catch(Exception e){
                    throw new RequestValidationException("Invalid email or password");
                }
                return toAuthResponse(user, jwtService.generateToken(user));
    }

    // helper
    private void savePasswordHistory(User user, String encondedPassword) {
        PasswordHistory ph = new PasswordHistory();
        ph.setUser(user);
        ph.setHashedPassword(encondedPassword);
        passwordHistoryRepository.save(ph);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .suspended(user.getSuspended())
                .build();
    }

}
