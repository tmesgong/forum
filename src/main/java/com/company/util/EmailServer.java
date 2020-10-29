package com.company.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailServer {
    private final String sendFrom;
    private final JavaMailSender mailSender;

    public EmailServer(@Value("${com.company.forum.email.sendFrom}") String sendFrom, JavaMailSender mailSender) {

        this.sendFrom = sendFrom;
        this.mailSender = mailSender;
    }

    public void sendTo(String email, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(sendFrom);
        helper.setTo(email);
        helper.setText(content);
        mailSender.send(mimeMessage);
    }
}
