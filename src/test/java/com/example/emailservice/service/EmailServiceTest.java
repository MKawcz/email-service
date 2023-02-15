package com.example.emailservice.service;

import com.example.emailservice.model.Email;
import com.example.emailservice.repository.EmailRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private EmailService emailService;
    @Mock
    private EmailRepository emailRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(emailRepository, javaMailSender);
    }

    @Test
    void Should_SaveEmailWithValidAddress() {
        // given
        Email email = new Email();
        email.setAddress("test@test.com");

        // when
        when(emailRepository.save(any(Email.class))).thenReturn(email);
        Email savedEmail = emailService.saveEmail(email);

        // then
        assertEquals(email, savedEmail);
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    void Should_ThrowConstraintViolationExceptionIfAddressIsInvalid() {
        Email invalidEmail = new Email();
        invalidEmail.setAddress("email@invalid");

        when(emailRepository.save(invalidEmail)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> {
            emailService.saveEmail(invalidEmail);
        });
    }

    @Test
    void Should_ThrowRuntimeExceptionIfEmailExists() {
        // given
        String email = "test@test.com";
        Email existingEmail = new Email();
        existingEmail.setIdEmail(1L);
        existingEmail.setAddress(email);
        Email newEmail = new Email();
        newEmail.setAddress(email);

        // when
        when(emailRepository.findByAddress(email)).thenReturn(List.of(existingEmail));

        // then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            emailService.saveEmail(newEmail);
        });

        String expectedMessage = "This email address is already taken: " + email;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void updateEmail() {
    }

    @Test
    void getAllEmails() {
    }

    @Test
    void deleteEmailById() {
    }

    @Test
    void getEmailById() {
    }

    @Test
    void sendEmailToAll() {
    }
}