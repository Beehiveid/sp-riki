package com.beehive.riki.sto;

import com.beehive.riki.common.ControllerFactoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sto")
public class StoController extends ControllerFactoryAdapter<Sto,Long> {
    @Autowired
    private StoServiceImpl stoService;

    @Override
    @GetMapping
    public List<Sto> index() {
        return stoService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Sto findById(@PathVariable Long id) {
        return stoService.findById(id);
    }

    @Override
    @PostMapping
    public void create(@RequestBody Sto sto) {
        stoService.save(sto);
    }
}
