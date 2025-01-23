package com.mindhub.rp_sp1.orders.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link com.mindhub.rp_sp1.orders.models.OrderItem}
 */
public record OrderItemDTO(
        Long id,
        @NotNull
        @Positive
        Long productId,
        @NotNull
        @Positive
        Integer quantity
) implements Serializable {
}