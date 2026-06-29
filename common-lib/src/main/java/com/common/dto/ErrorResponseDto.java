package com.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private boolean success;
    private String message;
    private Map<String,String> errors;
    private LocalDateTime timeStamp;
}
