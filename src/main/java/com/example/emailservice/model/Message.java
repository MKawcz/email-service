package com.example.emailservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class Message {
    @NotBlank(message = "Subject cannot be blank")
    private String subject;
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private List<String> pathsToAttachments;
}
