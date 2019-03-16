package com.beehive.riki.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class Scheduler {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

}
