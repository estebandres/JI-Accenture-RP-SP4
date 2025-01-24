package com.mindhub.rp_sp1.orders.dtos;

import com.mindhub.rp_sp1.orders.models.OrderStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.mindhub.rp_sp1.orders.models.Order}
 */
public record OrderDTO(
        @Nullable
        @Positive
        Long id,
        @Nullable
        @Positive
        Long userId,
        @NotNull
        @NotEmpty
        List<OrderItemDTO> items,
        OrderStatus status) implements Serializable {
}