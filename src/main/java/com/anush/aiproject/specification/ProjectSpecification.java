package com.anush.aiproject.specification;

import org.springframework.data.jpa.domain.Specification;

import com.anush.aiproject.dto.request.ProjectFilterRequest;
import com.anush.aiproject.entity.Project;

public class ProjectSpecification extends BaseSpecification<Project>{

    private Long userId;
    private final ProjectFilterRequest filter;

    private ProjectSpecification(Long userId,ProjectFilterRequest filter){
        this.filter = filter;
        this.userId = userId;
    }
    
    public static Specification<Project> build(Long userId,ProjectFilterRequest filter){
        return new ProjectSpecification(userId,filter).toSpec();
    }

    private Specification<Project> toSpec() {
       return combine(
                    // Mandatory — user can only ever see their own projects
            equalNested("user", "id", userId),

            // Optional — only applied when frontend sends ?status=ACTIVE
            equal("status", filter != null ? filter.getStatus() : null),

            // Optional — searches title AND description at once
            likeAny(filter != null ? filter.getSearch() : null, "title", "description")
       );
    }
}

