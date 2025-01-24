package com.mindhub.rp_sp1.products.controllers;

import com.mindhub.rp_sp1.products.dtos.PatchProductDTO;
import com.mindhub.rp_sp1.products.dtos.ProductDTO;
import com.mindhub.rp_sp1.products.exceptions.ProductMultiGetNoResultsException;
import com.mindhub.rp_sp1.products.exceptions.ProductNotFoundException;
import com.mindhub.rp_sp1.products.models.Product;
import com.mindhub.rp_sp1.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) String ids) throws ProductMultiGetNoResultsException {
        if (ids == null || ids.isEmpty()) {
            return productService.getAllProducts();
        }
        return productService.getAllProductsWithIds(ids);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) throws ProductNotFoundException {
        return productService.getProductWithId(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) throws ProductNotFoundException {
        return productService.updateProduct(id, productDTO);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @PatchMapping("/{id}")
    public Product partialUpdateProduct(@PathVariable Long id, @Valid @RequestBody PatchProductDTO product) throws ProductNotFoundException {
        return productService.updateProductAttributesWithId(id, product);
    }

}
