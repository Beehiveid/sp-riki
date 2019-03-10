package com.beehive.riki.agency;

import com.beehive.riki.common.ControllerFactoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency")
public class AgencyController extends ControllerFactoryAdapter<Agency,Long> {
    @Autowired
    private AgencyServiceImpl agencyService;

    @Override
    @GetMapping
    public List<Agency> index() {
        return agencyService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Agency findById(@PathVariable Long id) {
        return agencyService.findById(id);
    }

    @Override
    @PostMapping
    public void create(@RequestBody Agency agency) {
        agencyService.save(agency);
    }
}
