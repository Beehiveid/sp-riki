package com.beehive.riki.order;

import com.beehive.riki.common.ControllerFactoryAdapter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/order")
@Api(description = "Operation that handle orders module", tags = "Order")
public class OrderController extends ControllerFactoryAdapter<Order, Long> {
    @Autowired
    private OrderServiceImpl orderService;

    @Override
    @GetMapping
    public List<Order> index() {
        return orderService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Order findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @Override
    @PostMapping
    public void create(@RequestBody Order order) {
        orderService.save(order);
    }
}
