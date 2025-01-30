package com.mindhub.rp_sp1.orders.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.rp_sp1.orders.RabbitConfig;
import com.mindhub.rp_sp1.orders.dtos.*;
import com.mindhub.rp_sp1.orders.exceptions.*;
import com.mindhub.rp_sp1.orders.models.Order;
import com.mindhub.rp_sp1.orders.models.OrderItem;
import com.mindhub.rp_sp1.orders.models.OrderStatus;
import com.mindhub.rp_sp1.orders.repositories.OrderItemRepository;
import com.mindhub.rp_sp1.orders.repositories.OrderRepository;
import com.mindhub.rp_sp1.orders.services.OrderService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.logging.LoggingRebinder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);
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

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoggingRebinder loggingRebinder;

    public OrderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


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
        LOGGER.info("Order created with id: {}", savedOrder.getId());
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
            LOGGER.error("User with email {} not found", email);
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
                        LOGGER.error("Order {} contains non-existent products", orderDto.id());
                        throw new OrderContainsNonexistentProductsException();
                    }
                    if(stockIsInsufficient(products, orderDto.items())) {
                        LOGGER.error("Stock is insufficient for Order {}", orderDto.id());
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
                LOGGER.error("User with email {} was retrieved as a null value.", email);
                throw new SiteUserNotFoundException(email);
            }
        } else {
            LOGGER.error("User with email {} produces an empty array of users", email);
            throw new SiteUserNotFoundException(email);
        }
        LOGGER.info("Order successfully created: {}", responseOrderDTO);
        sendToRabbit(responseOrderDTO);
        return responseOrderDTO;
    }

    private void sendToRabbit(OrderDTO responseOrderDTO) {
        String orderDTOJson;
        try {
            orderDTOJson = new ObjectMapper().writeValueAsString(responseOrderDTO);
        }catch (Exception e) {
            LOGGER.error("Error while converting OrderDTO to JSON", e);
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_QUEUE_EXCHANGE,
                RabbitConfig.ORDERS_ROUTING_KEY,
                orderDTOJson,
                message -> {
                    message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    return message;
                });
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
        Order order = orderRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Order with id {} not found", id);
            return new OrderNotFoundException(id);
        });
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
        Order order = orderRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Order with id {} not found", id);
            return new OrderNotFoundException(id);
        });
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
        Order order = orderRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Order with id {} not found", id);
            return new OrderNotFoundException(id);
        });
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDTO confirmOrder(Long id) throws
            OrderNotFoundException,
            InsufficientStockForOrderCompletionException,
            NoResultsForBatchStockUpdateException {
        Order order = orderRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Order with id {} not found", id);
            return new OrderNotFoundException(id);
        });
        List<StockPatchDTO> stockPatchDTOS = order.getItems().stream().map(orderItem -> new StockPatchDTO(orderItem.getProductId(), orderItem.getQuantity())).toList();
        ProductDTO[] products;
        try{
            products = restTemplate.postForObject(productsServiceUrl + "/products/batch-stock", stockPatchDTOS, ProductDTO[].class);
        } catch (HttpClientErrorException.BadRequest ex){
            LOGGER.error("Stock is insufficient for Order {}", id);
            throw new InsufficientStockForOrderCompletionException();
        }
        if (products == null || products.length == 0) {
            LOGGER.error("No results for batch stock update for Order {}", id);
            throw new NoResultsForBatchStockUpdateException();
        }
        double totalAmount = calculateTotalAmount(order,products);
        order.setStatus(OrderStatus.COMPLETED);
        Order savedOrder = orderRepository.save(order);
        OrderDTO responseOrderDTO = new OrderDTO(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity())).toList(),
                savedOrder.getStatus(),
                totalAmount);
        sendToRabbit(responseOrderDTO);
        LOGGER.info("Order successfully completed: {}", responseOrderDTO);
        return responseOrderDTO;
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
