package com.mindhub.rp_sp1.products.dtos;

import com.mindhub.rp_sp1.products.models.Product;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link Product}
 */
public record PatchProductDTO(
        Long id,
        @Nullable
        @Pattern(regexp = "^\\s*\\S.*$", message = "Name must not be blank.")
        @Size(min = 3, max = 20 , message = "Name must be between 3 and 20 characters")
        String name,
        @Nullable
        @Pattern(regexp = "^\\s*\\S.*$", message = "Name must not be blank.")
        @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
        String description,
        @Positive
        Double price,
        @PositiveOrZero
        Integer stock
) implements Serializable {
}