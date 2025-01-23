package com.mindhub.rp_sp1.products.services;

import com.mindhub.rp_sp1.products.dtos.ProductDTO;
import com.mindhub.rp_sp1.products.dtos.PatchProductDTO;
import com.mindhub.rp_sp1.products.exceptions.ProductNotFoundException;
import com.mindhub.rp_sp1.products.models.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);
    Product updateProduct(Long id, ProductDTO productDTO) throws ProductNotFoundException;
    Product updateProductAttributesWithId(Long id, PatchProductDTO product) throws ProductNotFoundException;
    void deleteProduct(Long id) throws ProductNotFoundException;

    List<Product> getAllProducts();

    Product getProductWithId(Long id) throws ProductNotFoundException;
}
