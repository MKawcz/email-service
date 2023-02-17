package com.example.emailservice.service;

import com.example.emailservice.model.Email;
import com.example.emailservice.model.Message;
import com.example.emailservice.repository.EmailRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.*;

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
    }

    @Test
    void Should_ThrowConstraintViolationExceptionIfAddressIsInvalid() {
        // given
        Email invalidEmail = new Email();
        invalidEmail.setAddress("email@invalid");

        // when
        when(emailRepository.save(invalidEmail)).thenThrow(ConstraintViolationException.class);

        // then
        assertThrows(ConstraintViolationException.class, () -> {
            emailService.saveEmail(invalidEmail);
        });
    }

    @Test
    void Should_ThrowIllegalArgumentExceptionIfEmailExists() {
        // given
        String email = "test@test.com";
        Email existingEmail = new Email();
        existingEmail.setIdEmail(1L);
        existingEmail.setAddress(email);


        // when
        when(emailRepository.findByAddress(email)).thenReturn(List.of(existingEmail));

        // then
        Email newEmail = new Email();
        newEmail.setAddress(email);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.saveEmail(newEmail);
        });

        assertEquals("This email address is already taken: " + email, exception.getMessage());
    }

    @Test
    void Should_ReturnEmailByCorrectId() {
        // given
        Email email = new Email();
        email.setIdEmail(1L);
        email.setAddress("test@test.com");

        // when
        when(emailRepository.findById(email.getIdEmail())).thenReturn(Optional.of(email));

        Email retrievedEmail = emailService.getEmailById(email.getIdEmail());

        // then
        assertNotNull(retrievedEmail);
        assertEquals(email.getIdEmail(), retrievedEmail.getIdEmail());
        assertEquals(email.getAddress(), retrievedEmail.getAddress());
    }

    @Test
    void Should_ThrowEntityNotFoundExceptionIfEmailIdNotFound() {
        // given
        long invalidId = 12345L;

        // when
        when(emailRepository.findById(invalidId)).thenReturn(Optional.empty());

        // then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            emailService.getEmailById(invalidId);
        });

        assertEquals("Email with given id does not exists", exception.getMessage());
    }

    @Test
    void Should_UpdateValidEmail() {
        // given
        Email email = new Email();
        email.setAddress("test@test.com");

        // when
        when(emailRepository.findById(99L)).thenReturn(Optional.of(new Email()));
        when(emailRepository.save(any(Email.class))).thenReturn(email);

        // then
        Email updatedEmail = emailService.updateEmail(99L, email);
        assertEquals(updatedEmail, email);
    }

    @Test
    void Should_ReturnAllEmails() {
        // given
        List<Email> emailsExpected = new ArrayList<>();
        emailsExpected.add(new Email());
        emailsExpected.add(new Email());
        emailsExpected.add(new Email());

        // when
        when(emailRepository.findAll()).thenReturn(emailsExpected);

        List<Email> emailsActual = emailService.getAllEmails();

        // then
        assertEquals(emailsExpected, emailsActual);
    }

    @Test
    void Should_DeleteEmailByCorrectId() {
        // given
        Email email = new Email();
        email.setIdEmail(1L);
        email.setAddress("test@test.com");

        // when
        when(emailRepository.findById(email.getIdEmail())).thenReturn(Optional.of(email));

        emailService.deleteEmailById(email.getIdEmail());

        // then
        verify(emailRepository).delete(email);
    }

    @Test
    void Should_ThrowIllegalAccessExceptionWhenSendingEmailButEmailRepositoryIsEmpty() {
        // given
        Message message = new Message();
        message.setSubject("Subject");
        message.setContent("Content");

        // when
        when(emailRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            emailService.sendEmailToAll(message);
        });

        assertEquals("There is no emails in database!", exception.getMessage());
    }
}