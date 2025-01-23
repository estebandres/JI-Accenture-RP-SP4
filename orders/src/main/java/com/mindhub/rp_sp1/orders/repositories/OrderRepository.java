package com.mindhub.rp_sp1.orders.repositories;

import com.mindhub.rp_sp1.orders.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}