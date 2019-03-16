package com.beehive.riki.salesPerson;

import com.beehive.riki.common.ControllerFactoryAdapter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales/person")
@Api(description = "Operation that handle sales person module", tags = "Sales Person")
public class SalesPersonController extends ControllerFactoryAdapter<SalesPerson,Long> {
    @Autowired
    private SalesPersonServiceImpl salesPersonService;

    @Override
    @GetMapping
    public List<SalesPerson> index() {
        return salesPersonService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public SalesPerson findById(@PathVariable Long id) {
        return salesPersonService.findById(id);
    }

    @Override
    @PostMapping
    public void create(@RequestBody SalesPerson salesPerson) {
        salesPersonService.save(salesPerson);
    }
}
