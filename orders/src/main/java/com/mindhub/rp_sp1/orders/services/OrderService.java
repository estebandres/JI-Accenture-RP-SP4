package com.mindhub.rp_sp1.orders.services;

import com.mindhub.rp_sp1.orders.dtos.OrderDTO;
import com.mindhub.rp_sp1.orders.dtos.PatchOrderDTO;
import com.mindhub.rp_sp1.orders.exceptions.OrderNotFoundException;
import com.mindhub.rp_sp1.orders.exceptions.SiteUserNotFoundException;
import com.mindhub.rp_sp1.orders.models.Order;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(Long id) throws OrderNotFoundException;

    OrderDTO createOrder(@Valid OrderDTO orderDto);

    OrderDTO createOrder(@Valid OrderDTO orderDto, String email) throws SiteUserNotFoundException;

    OrderDTO updateOrder(Long id, @Valid OrderDTO orderDto) throws OrderNotFoundException;

    OrderDTO orderPartialUpdate(Long id, @Valid PatchOrderDTO patchOrderDto) throws OrderNotFoundException;

    void deleteOrder(Long id) throws OrderNotFoundException;
}
