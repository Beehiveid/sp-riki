package com.beehive.riki.customer;

import com.beehive.riki.common.ControllerFactoryAdapter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Api(description = "Operation that handle customers module", tags = "Customer")
public class CustomerController extends ControllerFactoryAdapter<Customer, Long> {
    @Autowired
    private CustomerServiceImpl customerService;

    @Override
    @GetMapping
    public List<Customer> index() {
        return customerService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @Override
    @PostMapping
    public void create(@RequestBody Customer customer) {
        customerService.save(customer);
    }
}
