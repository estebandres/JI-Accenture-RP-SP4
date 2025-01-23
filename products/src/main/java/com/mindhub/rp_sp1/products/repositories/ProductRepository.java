package com.mindhub.rp_sp1.products.repositories;

import com.mindhub.rp_sp1.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}