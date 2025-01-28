package com.mindhub.rp_sp4.mailman;

import com.mindhub.rp_sp4.mailman.dtos.OrderDTO;
import com.mindhub.rp_sp4.mailman.dtos.UserDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {
    @RabbitListener(queues = RabbitConfig.ORDERS_QUEUE_NAME)
    public void receiveNewOrderMessage(OrderDTO newOrder) {
        System.out.println("Received order: " + newOrder);
    }

    public void receiveNewUserMessage(UserDTO newUser) {
        System.out.println("Received new user: " + newUser);
    }
}
