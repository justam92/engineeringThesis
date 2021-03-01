package com.engineering.thesis.backend.service;

import javax.activation.DataSource;

public interface EmailSenderService {
    void sendEmail(String to, String title, String content);
    void sendEmailWithAttachment(String to, String title, String content, String fileName, DataSource file);
}
