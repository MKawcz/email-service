package com.example.emailservice.service;

import com.example.emailservice.model.Email;
import com.example.emailservice.model.Message;
import com.example.emailservice.repository.EmailRepository;
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
            emailToUpdate.get().setAddress(updatedEmail.getAddress());
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
            mailMessage.setFrom("kmikolaj49@gmail.com");
            mailMessage.setTo(email.getAddress());
            mailMessage.setSubject(message.getSubject());
            mailMessage.setText(message.getContent());

            javaMailSender.send(mailMessage);
        }
    }

}
