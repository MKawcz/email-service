package com.example.emailservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Message {
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
}
