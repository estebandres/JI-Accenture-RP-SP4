package com.mindhub.rp_sp1.orders.services.impl;

import com.mindhub.rp_sp1.orders.dtos.OrderDTO;
import com.mindhub.rp_sp1.orders.dtos.OrderItemDTO;
import com.mindhub.rp_sp1.orders.dtos.PatchOrderDTO;
import com.mindhub.rp_sp1.orders.exceptions.OrderNotFoundException;
import com.mindhub.rp_sp1.orders.exceptions.SiteUserNotFoundException;
import com.mindhub.rp_sp1.orders.models.Order;
import com.mindhub.rp_sp1.orders.models.OrderItem;
import com.mindhub.rp_sp1.orders.models.OrderStatus;
import com.mindhub.rp_sp1.orders.repositories.OrderItemRepository;
import com.mindhub.rp_sp1.orders.repositories.OrderRepository;
import com.mindhub.rp_sp1.orders.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getItems().stream().map(orderItem -> new OrderItemDTO(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity())).toList(),
                order.getStatus())).toList();
    }

    @Override
    public OrderDTO getOrderById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return new OrderDTO(order.getId(), order.getUserId(), order.getItems().stream().map(orderItem -> new OrderItemDTO(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity())).toList(), order.getStatus());
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDto) {
        Order order = new Order();
        order.setUserId(orderDto.userId());
        if (orderDto.status() != null) {
            order.setStatus(orderDto.status());
        }
        if (orderDto.items() != null) {
            order.setItems(orderDto.items().stream()
                    .map(orderItemDTO -> {
                        OrderItem item = new OrderItem(orderItemDTO.productId(), orderItemDTO.quantity());
                        item.setOrder(order);
                        orderItemRepository.save(item);
                        return item;
                    })
                    .toList());
        }
        Order savedOrder =  orderRepository.save(order);
        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity()
                        )).toList(),
                savedOrder.getStatus()
        );
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDto, String email) throws SiteUserNotFoundException {
        return null;
    }


    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDto) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setUserId(orderDto.userId());
        if (orderDto.status() != null) {
            order.setStatus(orderDto.status());
        }
        if (orderDto.items() != null) {
            order.setItems(orderDto.items().stream()
                    .map(orderItemDTO -> {
                        OrderItem item = new OrderItem(orderItemDTO.productId(), orderItemDTO.quantity());
                        item.setOrder(order);
                        orderItemRepository.save(item);
                        return item;
                    })
                    .toList());
        }
        Order savedOrder =  orderRepository.save(order);
        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity()
                        )).toList(),
                savedOrder.getStatus()
        );
    }

    @Override
    @Transactional
    public OrderDTO orderPartialUpdate(Long id, PatchOrderDTO patchOrderDto) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (patchOrderDto.userId() != null) {
            order.setUserId(patchOrderDto.userId());
        }
        if (patchOrderDto.status() != null) {
            order.setStatus(patchOrderDto.status());
        }
        if (patchOrderDto.items() != null) {
            order.setItems(patchOrderDto.items().stream()
                    .map(orderItemDTO -> {
                        OrderItem item = new OrderItem(orderItemDTO.productId(), orderItemDTO.quantity());
                        item.setOrder(order);
                        orderItemRepository.save(item);
                        return item;
                    })
                    .toList());
        }
        Order savedOrder = orderRepository.save(order);

        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity())).toList(),
                savedOrder.getStatus());
    }

    @Override
    public void deleteOrder(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.deleteById(id);
    }

}
