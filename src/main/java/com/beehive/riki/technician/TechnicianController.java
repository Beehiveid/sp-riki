package com.beehive.riki.technician;

import com.beehive.riki.common.ControllerFactoryAdapter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/technician")
@Api(description = "Operation that handle technicians module", tags = "Technician")
public class TechnicianController extends ControllerFactoryAdapter<Technician,Long> {
    @Autowired
    private TechnicianServiceImpl technicianService;

    @Override
    @GetMapping
    public List<Technician> index() {
        return technicianService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Technician findById(@PathVariable Long id) {
        return technicianService.findById(id);
    }

    @Override
    @PostMapping
    public void create(@RequestBody Technician technician) {
        technicianService.save(technician);
    }
}
