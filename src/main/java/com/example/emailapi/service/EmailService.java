package com.example.emailapi.service;

import com.example.emailapi.model.Email;
import com.example.emailapi.model.Message;
import com.example.emailapi.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    public Email saveEmail(Email email) {
        return emailRepository.save(email);
    }

    public Email updateEmail(long idEmail, Email updatedEmail) {
        var emailToUpdate = emailRepository.findById(idEmail);
        if (emailToUpdate.isPresent()) {
            emailToUpdate.get().setEmail(updatedEmail.getEmail());
            emailRepository.save(emailToUpdate.get());
        }
        throw new RuntimeException("Email with given id does not exists");
    }

    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    public void deleteEmailById(long idEmail) {
        try {
            emailRepository.deleteById(idEmail);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Email with given id does not exists");
        }
    }

    public Email getEmailById(long idEmail) {
        var optionalEmail = emailRepository.findById(idEmail);

        if(optionalEmail.isPresent()) {
            return optionalEmail.get();
        }

        throw new RuntimeException("Email with given id does not exists");
    }

    public void sendEmailToAll(Message message) {
        for (Email email : emailRepository.findAll()){
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email.getEmail());
            mailMessage.setSubject(message.getSubject());
            mailMessage.setText(message.getContent());
            javaMailSender.send(mailMessage);
        }
    }

}
