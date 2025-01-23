package com.mindhub.rp_sp1.products.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String name;

    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer stock;

    public Product( String name,  String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
