package com.anush.aiproject.specification;

import org.springframework.data.jpa.domain.Specification;

import com.anush.aiproject.dto.request.TaskFilterRequest;
import com.anush.aiproject.entity.Task;

public class TaskSpecification extends BaseSpecification<Task> {

    private Long projectId;
    private final TaskFilterRequest filter;

    private TaskSpecification(Long projectId, TaskFilterRequest filter) {
        this.projectId = projectId;
        this.filter = filter;
    }

    public static Specification build(Long projectId, TaskFilterRequest filter) {
        return new TaskSpecification(projectId, filter).toSpec();
    }

    private Specification<Task> toSpec() {
        // safe access — filter can be null if no query params were sent
        TaskFilterRequest f = filter != null ? filter : new TaskFilterRequest();

        return combine(
                // Always required — tasks scoped to this project
                equalNested("project", "id", projectId),

                // Optional filters
                equal("status", f.getStatus()),
                equal("priority", f.getPriority()),
                like("title", f.getSearch()),

                // Date range — ?deadlineAfter=2025-01-01T00:00:00
                greaterThanOrEqual("deadline", f.getDeadlineAfter()),
                lessThanOrEqual("deadline", f.getDeadlineBefore()));
    }

}
