package com.anush.aiproject.service.impl;
import com.anush.aiproject.shared.constants.EmailConstants;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.anush.aiproject.email.EmailTemplateService;
import com.anush.aiproject.service.EmailService;

@Service
@Primary
@RequiredArgsConstructor
public class ResendEmailServiceImpl implements EmailService {

    private final Resend resend;
    private final EmailTemplateService templateService;
    
    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {

        //load the email template and replace variables
       String html=templateService.loadTemplate(templateName, variables);

      CreateEmailOptions options = CreateEmailOptions.builder()
                .from(EmailConstants.FROM)
                .to(to)
                .subject(subject)
                .html(html)
                .build();

                try{
                    CreateEmailResponse response = resend.emails().send(options);
                    System.out.println("Email sent successfully: " + response.getId());
                }
                catch(Exception e){
                    System.err.println("Failed to send email: " + e.getMessage());
                }


        
    }
    
}
