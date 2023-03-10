package com.example.emailservice.controller;

import com.example.emailservice.model.Email;
import com.example.emailservice.model.Message;
import com.example.emailservice.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Email> saveEmail(@Valid @RequestBody Email email) {
        Email savedEmail = emailService.saveEmail(email);
        return ResponseEntity.ok(savedEmail);
    }

    @GetMapping("/{emailId}")
    public ResponseEntity<Email> getEmailById(@PathVariable long emailId) {
        return ResponseEntity.ok(emailService.getEmailById(emailId));
    }

    @PutMapping("/{emailId}")
    public ResponseEntity<Email> updateEmail(@PathVariable long emailId,
                                             @Valid @RequestBody Email email
    ) {
       return ResponseEntity.ok(emailService.updateEmail(emailId, email));
    }

    @GetMapping
    public ResponseEntity<List<Email>> getAllEmails() {
        return ResponseEntity.ok(emailService.getAllEmails());
    }

    @DeleteMapping("/{emailId}")
    public ResponseEntity<String> deleteEmailById(@PathVariable long emailId) {
        emailService.deleteEmailById(emailId);
        return ResponseEntity.ok("Email with id: " + emailId + " has been deleted");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmailToAll(@Valid @RequestBody Message message) {
        emailService.sendEmailToAll(message);
        return ResponseEntity.ok("Email \"" + message.getSubject() + "\" sent to all addresses successfully!");
    }
}
