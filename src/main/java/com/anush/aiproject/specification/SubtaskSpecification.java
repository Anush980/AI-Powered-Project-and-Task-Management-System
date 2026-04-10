package com.anush.aiproject.specification;

import com.anush.aiproject.dto.request.SubtaskFilterRequest;
import com.anush.aiproject.entity.Subtask;
import org.springframework.data.jpa.domain.Specification;

public class SubtaskSpecification extends BaseSpecification<Subtask> {

    private final Long taskId;
    private final SubtaskFilterRequest filter;

    private SubtaskSpecification(Long taskId, SubtaskFilterRequest filter) {
        this.taskId = taskId;
        this.filter = filter;
    }

    public static Specification<Subtask> build(Long taskId, SubtaskFilterRequest filter) {
        return new SubtaskSpecification(taskId, filter).toSpec();
    }

    private Specification<Subtask> toSpec() {
        SubtaskFilterRequest f = filter != null ? filter : new SubtaskFilterRequest();
        return combine(
            equalNested("task", "id", taskId),
            equal("status", f.getStatus()),
            like("title",   f.getSearch())
        );
    }
}