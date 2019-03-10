package com.beehive.riki.configuration;

import com.beehive.riki.system.SystemEnvironment;
import com.beehive.riki.system.SystemEnvironmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {
    @Autowired
    private SystemEnvironmentServiceImpl systemEnvironmentService;

    @GetMapping("/priority")
    public SystemEnvironment priorityConfiguration(){
        return systemEnvironmentService.loadSROPriorityConfig();
    }
}
