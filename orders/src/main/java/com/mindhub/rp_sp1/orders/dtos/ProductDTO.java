package com.mindhub.rp_sp1.orders.dtos;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public record ProductDTO(
        Long id,
        @NotNull(message = "Name is required")
        @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
        @NotEmpty(message = "Name must be not empty")
        String name,
        @NotNull(message = "Description is required")
        @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
        @NotEmpty(message = "Description must be not empty")
        String description,
        @NotNull(message = "Price is required")
        @Positive
        Double price,
        @PositiveOrZero
        Integer stock
) implements Serializable {
}