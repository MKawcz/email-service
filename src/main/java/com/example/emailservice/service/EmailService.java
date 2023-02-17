package com.example.emailservice.service;

import com.example.emailservice.model.Email;
import com.example.emailservice.model.Message;
import com.example.emailservice.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    public Email saveEmail(Email email) {
        validateUniqueEmail(email);
        return emailRepository.save(email);
    }

    public Email getEmailById(long idEmail) {
        var optionalEmail = emailRepository.findById(idEmail);

        if(optionalEmail.isEmpty()) {
            throw new EntityNotFoundException("Email with given id does not exists");
        }

        return optionalEmail.get();
    }
    public Email updateEmail(long idEmail, Email updatedEmail) {
        var emailToUpdate = getEmailById(idEmail);

        validateUniqueEmail(updatedEmail);
        emailToUpdate.setAddress(updatedEmail.getAddress());

        return emailRepository.save(emailToUpdate);
    }

    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    public void deleteEmailById(long idEmail) {
        var emailToDelete = getEmailById(idEmail);
        emailRepository.delete(emailToDelete);
    }

    public void sendEmailToAll(Message message) {
        if (emailRepository.findAll().isEmpty()) {
            throw new IllegalStateException("There is no emails in database!");
        }

        for (Email email : emailRepository.findAll()){
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom("kmikolaj49@gmail.com");
                helper.setTo(email.getAddress());
                helper.setSubject(message.getSubject());
                helper.setText(message.getContent());

                if (message.getPathsToAttachments() != null) {
                    for (String path : message.getPathsToAttachments()) {
                        FileSystemResource file = new FileSystemResource(new File(path));
                        helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
                    }
                }

                javaMailSender.send(mimeMessage);

            } catch (MessagingException | MailSendException | IllegalArgumentException e) {
                throw new IllegalStateException("Failed to send the message!", e);
            }
        }
    }

    private void validateUniqueEmail(Email email) {
        if(!emailRepository.findByAddress(email.getAddress()).isEmpty()) {
            throw new IllegalArgumentException("This email address is already taken: " + email.getAddress());
        }
    }
}
