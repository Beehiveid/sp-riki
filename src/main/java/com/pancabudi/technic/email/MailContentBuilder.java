package com.pancabudi.technic.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;

@Service
public class MailContentBuilder {
    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    String builder(String template, Context context){
        String result = LOWER_HYPHEN.to(LOWER_CAMEL,template);
        return templateEngine.process(result, context);
    }
}
