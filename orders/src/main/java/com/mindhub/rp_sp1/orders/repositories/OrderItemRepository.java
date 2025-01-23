package com.mindhub.rp_sp1.orders.repositories;

import com.mindhub.rp_sp1.orders.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}