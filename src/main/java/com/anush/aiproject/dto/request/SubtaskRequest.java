package com.anush.aiproject.dto.request;

import com.anush.aiproject.shared.constants.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubtaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private TaskStatus status;
}
