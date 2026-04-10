package com.anush.aiproject.email;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailTemplateService {

    public String loadTemplate(String templateName, Map<String, Object> variables) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email/" + templateName + ".html");

            String content = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                content = content.replace(
                        "{{" + entry.getKey() + "}}",
                        entry.getValue().toString()
                );
            }

            return content;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
}