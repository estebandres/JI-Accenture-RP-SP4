package com.mindhub.rp_sp4.mailman.dtos;

import com.mindhub.rp_sp4.mailman.dtos.OrderItemDTO;

import java.io.Serializable;
import java.util.List;

public record OrderDTO(
        Long id,
        Long userId,
        List<OrderItemDTO> items,
        String status,
        Double total) implements Serializable {
}