package com.mindhub.rp_sp4.mailman;

import com.mindhub.rp_sp4.mailman.dtos.OrderDTO;
import com.mindhub.rp_sp4.mailman.dtos.UserDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {
    @Autowired
    EmailService emailService;
    @RabbitListener(queues = RabbitConfig.ORDERS_QUEUE_NAME)
    public void receiveNewOrderMessage(OrderDTO newOrder) {
        System.out.println("Received order: " + newOrder);
        emailService.sendEmail("morales.esteban.andres@gmail.com", "New Order Created", newOrder.toString());
    }

    public void receiveNewUserMessage(UserDTO newUser) {
        System.out.println("Received new user: " + newUser);
    }
}
