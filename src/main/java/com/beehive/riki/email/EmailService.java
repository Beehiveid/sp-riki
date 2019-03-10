package com.beehive.riki.email;

import com.pancabudi.technic.serviceRequestOrder.ServiceRequestOrder;
import com.pancabudi.technic.users.AppUser;
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

    @Async
    public void creatingSRO(Context context) {
        MailObject mailObject = new MailObject();
        mailObject.setTo(((AppUser) context.getVariable("technician")).getPerson().getEmail());
        mailObject.setTemplate("mail-creating-sro");
        mailObject.setSubject("New SRO:"+((ServiceRequestOrder) context.getVariable("sro")).getId());

        mailClient.send(mailObject, context);
    }

    @Async
    public void handlingSRO(Context context) {
        MailObject mailObject = new MailObject();
        mailObject.setTo(((ServiceRequestOrder) context.getVariable("sro")).getRequester().getEmail());
        mailObject.setTemplate("mail-handling-sro");
        mailObject.setSubject("Handling SRO:"+((ServiceRequestOrder) context.getVariable("sro")).getId());

        mailClient.send(mailObject, context);
    }
}
