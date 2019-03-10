package com.beehive.riki.common;

import com.beehive.riki.machine.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Scheduler {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

    @Autowired
    private MachineService machineService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void routineMaintenance(){
        String name = "Machine Maintenance Daily Checker";
        log.info("[{}] executed at {}",name , formatter.format(new Date()));
        machineService.routineMaintenance();
    }
}
