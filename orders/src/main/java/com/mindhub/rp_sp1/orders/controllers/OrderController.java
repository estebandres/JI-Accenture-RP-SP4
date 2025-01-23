package com.mindhub.rp_sp1.orders.controllers;

import com.mindhub.rp_sp1.orders.dtos.OrderDTO;
import com.mindhub.rp_sp1.orders.dtos.PatchOrderDTO;
import com.mindhub.rp_sp1.orders.exceptions.OrderNotFoundException;
import com.mindhub.rp_sp1.orders.exceptions.SiteUserNotFoundException;
import com.mindhub.rp_sp1.orders.models.Order;
import com.mindhub.rp_sp1.orders.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(Long id) throws OrderNotFoundException {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public OrderDTO createOrder(@Valid @RequestBody OrderDTO orderDto, @RequestParam(required = false) String email) throws SiteUserNotFoundException {
        if (email != null && !email.isEmpty()) {
            System.out.println("With email: " + email);
            return orderService.createOrder(orderDto, email);
        }
        System.out.println("create without email");
        return orderService.createOrder(orderDto);
    }

    @PutMapping("/{id}")
    public OrderDTO updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDto) throws OrderNotFoundException {
        return orderService.updateOrder(id, orderDto);
    }

    @PatchMapping("/{id}")
    public OrderDTO patchOrder(@PathVariable Long id, @Valid @RequestBody PatchOrderDTO patchOrderDto) throws OrderNotFoundException {
        return orderService.orderPartialUpdate(id, patchOrderDto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) throws OrderNotFoundException {
        orderService.deleteOrder(id);
    }
}
