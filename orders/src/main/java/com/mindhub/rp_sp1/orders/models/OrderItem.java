package com.mindhub.rp_sp1.orders.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    public OrderItem(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
