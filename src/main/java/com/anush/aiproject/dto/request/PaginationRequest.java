package com.anush.aiproject.dto.request;

import lombok.Data;

@Data
public class PaginationRequest {

    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
    
}
