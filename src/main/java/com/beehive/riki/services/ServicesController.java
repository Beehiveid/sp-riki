package com.beehive.riki.services;

import com.beehive.riki.common.ControllerFactoryAdapter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
@Api(description = "Operation that handle services module", tags = "Service")
public class ServicesController extends ControllerFactoryAdapter<Services,Long> {
    @Autowired
    private ServicesServiceImpl servicesService;

    @Override
    @GetMapping
    public List<Services> index() {
        return servicesService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Services findById(@PathVariable Long id) {
        return servicesService.findById(id);
    }

    @Override
    @PostMapping
    public void create(@RequestBody Services services) {
        servicesService.save(services);
    }
}
