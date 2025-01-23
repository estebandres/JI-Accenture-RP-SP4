package com.mindhub.rp_sp1.orders.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }
    public void setItems(List<OrderItem> newItems) {
        this.items.clear();
        for (OrderItem item : newItems) {
            this.addItem(item); // Ensures bidirectional relationship is maintained
        }
    }
}
