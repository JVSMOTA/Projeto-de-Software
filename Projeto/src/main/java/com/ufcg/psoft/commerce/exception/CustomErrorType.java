package com.ufcg.psoft.commerce.exception;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorType {

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<String> errors;
    
    public CustomErrorType(CommerceException e) {
        this.timestamp = LocalDateTime.now();
        this.message = e.getMessage();
        this.errors = new ArrayList<>();
    }

}
