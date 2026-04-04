package com.anush.aiproject.dto.response;

import java.time.LocalDateTime;
import com.anush.aiproject.shared.constants.TaskStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubtaskResponse {
    
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long taskId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
