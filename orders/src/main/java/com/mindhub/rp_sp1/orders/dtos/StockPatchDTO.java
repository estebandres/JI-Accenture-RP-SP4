package com.mindhub.rp_sp1.orders.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public record StockPatchDTO(@NotNull @Positive Long productId,
                            @NotNull @Positive Integer deduction) implements Serializable {}
