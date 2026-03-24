package com.anush.aiproject.dto.response;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Anush
 */

public record ApiResponse<T>(boolean success,String message,T data, List<String> errors,LocalDateTime timestamp) {

    //success
    public static <T> ApiResponse<T> success(T data, String message){
        return new ApiResponse<T>(true, message, data, null, LocalDateTime.now());
    }

        //failure
    public static <T> ApiResponse<T> failure(List<String> errors, String message){
        return new ApiResponse<T>(false, message, null, errors, LocalDateTime.now());
    }
    
}
