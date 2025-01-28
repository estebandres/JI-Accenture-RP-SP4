package com.mindhub.rp_sp4.mailman.dtos;

import java.io.Serializable;

public record OrderItemDTO(
        Long id,
        Long productId,
        Integer quantity
) implements Serializable {
}