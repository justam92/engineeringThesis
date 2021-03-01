package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(String to, String title, String content) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(title);
            helper.setText(content, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }

    @Override
    public void sendEmailWithAttachment(String to, String title, String content, String fileName, DataSource attachmentFile) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(title);
            helper.setText(content, false);
            helper.addAttachment(fileName, attachmentFile);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }
}
