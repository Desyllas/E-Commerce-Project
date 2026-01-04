package dev.desyllas.E_CommerceProject.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private Map<String, String> errors;
    private String path;
}
