package com.beehive.riki.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    @Autowired
    private MailClient mailClient;

    @Async
    public void passwordReset(Context context) {
        MailObject mailObject = new MailObject();
        mailObject.setTo(context.getVariable("email").toString());
        mailObject.setTemplate("mail-reset-password");
        mailObject.setSubject("Reset Password");

        mailClient.send(mailObject, context);
    }
}
