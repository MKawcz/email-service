package com.example.emailservice.service;

import com.example.emailservice.model.Email;
import com.example.emailservice.model.Message;
import com.example.emailservice.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    public Email saveEmail(Email email) {
        if(!emailRepository.findByAddress(email.getAddress()).isEmpty()) {
            throw new RuntimeException("This email address is already taken: " + email.getAddress());
        }
        return emailRepository.save(email);
    }

    public Email updateEmail(long idEmail, Email updatedEmail) {
        var emailToUpdate = emailRepository.findById(idEmail);

        if (emailToUpdate.isEmpty()) {
            throw new RuntimeException("Email with given id does not exists");
        }
        if(!emailRepository.findByAddress(updatedEmail.getAddress()).isEmpty()) {
            throw new RuntimeException("This email address is already taken: " + updatedEmail.getAddress());
        }

        emailToUpdate.get().setAddress(updatedEmail.getAddress());
        Email saved = emailRepository.save(emailToUpdate.get());
        return saved;
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

        if(optionalEmail.isEmpty()) {
            throw new RuntimeException("Email with given id does not exists");
        }
        return optionalEmail.get();
    }

    public void sendEmailToAll(Message message) {
        for (Email email : emailRepository.findAll()){
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom("kmikolaj49@gmail.com");
                helper.setTo(email.getAddress());
                helper.setSubject(message.getSubject());
                helper.setText(message.getContent());

                if(message.getPathToAttachment() != null) {
                    FileSystemResource file = new FileSystemResource(new File(message.getPathToAttachment()));
                    helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
                }

                javaMailSender.send(mimeMessage);

            } catch (MessagingException | MailSendException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to send the message!", e);
            }

        }
    }

}
