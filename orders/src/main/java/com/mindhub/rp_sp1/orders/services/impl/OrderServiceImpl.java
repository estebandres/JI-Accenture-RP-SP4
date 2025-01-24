package com.mindhub.rp_sp1.orders.services.impl;

import com.mindhub.rp_sp1.orders.dtos.*;
import com.mindhub.rp_sp1.orders.exceptions.*;
import com.mindhub.rp_sp1.orders.models.Order;
import com.mindhub.rp_sp1.orders.models.OrderItem;
import com.mindhub.rp_sp1.orders.models.OrderStatus;
import com.mindhub.rp_sp1.orders.repositories.OrderItemRepository;
import com.mindhub.rp_sp1.orders.repositories.OrderRepository;
import com.mindhub.rp_sp1.orders.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${users.service.url}")
    private String usersServiceUrl;

    @Value("${products.service.url}")
    private String productsServiceUrl;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getItems().stream().map(orderItem -> new OrderItemDTO(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity())).toList(),
                order.getStatus(),null)).toList();
    }

    @Override
    public OrderDTO getOrderById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return new OrderDTO(order.getId(), order.getUserId(), order.getItems().stream().map(orderItem -> new OrderItemDTO(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity())).toList(), order.getStatus(),null);
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
                savedOrder.getStatus(),null);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDto, String email) throws SiteUserNotFoundException, OrderContainsNonexistentProductsException, StockInsufficientException {
        OrderDTO responseOrderDTO = null;
        String url = usersServiceUrl + "/users?email=" + email;
        SiteUserDto[] users;
        try {
            users = restTemplate.getForObject(url, SiteUserDto[].class);
        } catch (HttpClientErrorException.NotFound ex){
            throw new SiteUserNotFoundException(email);
        }

        if(users != null && users.length > 0) {
            Long userId = users[0].id();
            if (userId != null) {
                //User exists and has valid id
                //if(orderDto.items() != null){throw new CreateOrderWithNoItemsException();}
                String productIds = orderDto.items().stream()
                        .map(orderItemDTO -> orderItemDTO.productId().toString())
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");
                //assert !productIds.isEmpty();
                ProductDTO[] products = restTemplate.getForObject(productsServiceUrl + "/products?ids=" + productIds, ProductDTO[].class);
                if (products != null && products.length > 0) {
                    if (products.length != orderDto.items().size()) {
                        throw new OrderContainsNonexistentProductsException();
                    }
                    if(stockIsInsufficient(products, orderDto.items())) {
                        throw new StockInsufficientException();
                    }
                    Order newOrder = new Order();
                    newOrder.setUserId(userId);
                    newOrder.setItems(orderDto.items().stream()
                            .map(orderItemDTO -> {
                                OrderItem item = new OrderItem(orderItemDTO.productId(), orderItemDTO.quantity());
                                item.setOrder(newOrder);
                                orderItemRepository.save(item);
                                return item;
                            })
                            .toList());
                    Order savedOrder = orderRepository.save(newOrder);
                    responseOrderDTO = new OrderDTO(
                            savedOrder.getId(),
                            savedOrder.getUserId(),
                            savedOrder.getItems().stream()
                                    .map(orderItem -> new OrderItemDTO(
                                            orderItem.getId(),
                                            orderItem.getProductId(),
                                            orderItem.getQuantity()
                                    )).toList(),
                            savedOrder.getStatus(),null);
                }
            } else {
                throw new SiteUserNotFoundException(email);
            }
        } else {
            throw new SiteUserNotFoundException(email);
        }
        return responseOrderDTO;
    }

    public boolean stockIsInsufficient(ProductDTO[] products, List<OrderItemDTO> orderItems) {
        for(OrderItemDTO item : orderItems) {
            for (ProductDTO p : products) {
                if (item.productId().equals(p.id())) {
                    if (p.stock() < item.quantity()) {
                        return true;
                    }
                }
            }
        }
        return false;
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
                savedOrder.getStatus(),null);
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
                savedOrder.getStatus(),null);
    }

    @Override
    public void deleteOrder(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDTO confirmOrder(Long id) throws
            OrderNotFoundException,
            InsufficientStockForOrderCompletionException,
            NoResultsForBatchStockUpdateException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        List<StockPatchDTO> stockPatchDTOS = order.getItems().stream().map(orderItem -> new StockPatchDTO(orderItem.getProductId(), orderItem.getQuantity())).toList();
        ProductDTO[] products;
        try{
            products = restTemplate.postForObject(productsServiceUrl + "/products/batch-stock", stockPatchDTOS, ProductDTO[].class);
        } catch (HttpClientErrorException.BadRequest ex){
            throw new InsufficientStockForOrderCompletionException();
        }
        if (products == null || products.length == 0) {
            throw new NoResultsForBatchStockUpdateException();
        }
        double totalAmount = calculateTotalAmount(order,products);
        order.setStatus(OrderStatus.COMPLETED);
        Order savedOrder = orderRepository.save(order);
        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity())).toList(),
                savedOrder.getStatus(),
                totalAmount);
    }

    private double calculateTotalAmount(Order order, ProductDTO[] products) {
        double totalAmount = 0.0;
        for (OrderItem item : order.getItems()) {
            for (ProductDTO product : products) {
                if (item.getProductId().equals(product.id())) {
                    totalAmount+= product.price() * item.getQuantity();
                }
            }
        }
        return totalAmount;
    }

}
