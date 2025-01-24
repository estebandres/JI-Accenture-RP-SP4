package com.mindhub.rp_sp1.products.services;

import com.mindhub.rp_sp1.products.dtos.ProductDTO;
import com.mindhub.rp_sp1.products.dtos.PatchProductDTO;
import com.mindhub.rp_sp1.products.dtos.StockPatchDTO;
import com.mindhub.rp_sp1.products.exceptions.InsufficientStockForBatchDeductionException;
import com.mindhub.rp_sp1.products.exceptions.ProductMultiGetNoResultsException;
import com.mindhub.rp_sp1.products.exceptions.ProductNotFoundException;
import com.mindhub.rp_sp1.products.models.Product;
import jakarta.validation.Valid;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);
    Product updateProduct(Long id, ProductDTO productDTO) throws ProductNotFoundException;
    Product updateProductAttributesWithId(Long id, PatchProductDTO product) throws ProductNotFoundException;
    void deleteProduct(Long id) throws ProductNotFoundException;

    List<Product> getAllProducts();

    Product getProductWithId(Long id) throws ProductNotFoundException;

    List<Product> getAllProductsWithIds(String ids) throws ProductMultiGetNoResultsException;

    List<Product> batchStockDeductions(@Valid List<StockPatchDTO> updates) throws InsufficientStockForBatchDeductionException;
}
