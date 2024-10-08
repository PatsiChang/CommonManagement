package com.common.email.service;

import com.common.email.bean.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Component
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final TemplateEngine templateEngine;


    @Async
    public void sendEmail(Email request) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom("noreply@smarthome.com");
        mimeMessageHelper.setTo(request.getEmailReceiver());
        mimeMessageHelper.setSubject(request.getSubject());

        if (request.isHTML()) {
            Context context = new Context();
            context.setVariable("receiverName", request.getReceiverName());
            context.setVariable("token", request.getToken());
            String processedString = templateEngine.process("emailVerification", context);
            mimeMessageHelper.setText(processedString, true);
        } else {
            mimeMessageHelper.setText(request.getMessage(), false);
        }
        mailSender.send(mimeMessage);

    }
}
