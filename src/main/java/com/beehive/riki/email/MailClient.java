package com.beehive.riki.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class MailClient {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    void send(MailObject mailObject, Context context) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setFrom("hris@pancabudi.com");
            messageHelper.setTo(mailObject.getTo());
            messageHelper.setSubject(mailObject.getSubject());

            String content = mailContentBuilder.builder(mailObject.getTemplate(), context);
            messageHelper.setText(content, true);
        };

        mailSender.send(messagePreparator);
    }
}
