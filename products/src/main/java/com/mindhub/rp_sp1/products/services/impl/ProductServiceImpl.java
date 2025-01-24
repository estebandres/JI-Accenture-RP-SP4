package com.mindhub.rp_sp1.products.services.impl;

import com.mindhub.rp_sp1.products.dtos.ProductDTO;
import com.mindhub.rp_sp1.products.dtos.PatchProductDTO;
import com.mindhub.rp_sp1.products.dtos.StockPatchDTO;
import com.mindhub.rp_sp1.products.exceptions.InsufficientStockForBatchDeductionException;
import com.mindhub.rp_sp1.products.exceptions.ProductMultiGetNoResultsException;
import com.mindhub.rp_sp1.products.exceptions.ProductNotFoundException;
import com.mindhub.rp_sp1.products.models.Product;
import com.mindhub.rp_sp1.products.services.ProductService;
import com.mindhub.rp_sp1.products.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
   @Autowired
   private ProductRepository productRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Product newProduct = new Product(productDTO.name(),
                productDTO.description(),
                productDTO.price());
        if (productDTO.stock() != null) {
            newProduct.setStock(productDTO.stock());
        }
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        if (productDTO.stock() != null) {
            product.setStock(productDTO.stock());
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProductAttributesWithId(Long id, PatchProductDTO product) throws ProductNotFoundException {
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        if (product.name() != null) {
            productToUpdate.setName(product.name());
        }
        if (product.description() != null) {
            productToUpdate.setDescription(product.description());
        }
        if (product.price() != null) {
            productToUpdate.setPrice(product.price());
        }
        if (product.stock() != null) {
            productToUpdate.setStock(product.stock());
        }
        return productRepository.save(productToUpdate);
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductWithId(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProductsWithIds(String ids) throws ProductMultiGetNoResultsException {
    List<Long> idList = Arrays.stream(ids.split(","))
                              .map(Long::parseLong)
                              .collect(Collectors.toList());

    List<Product> products = productRepository.findAllById(idList);

    if (products.isEmpty()) {
        throw new ProductMultiGetNoResultsException();
    }

    return products;
    }

    @Override
    @Transactional
    public List<Product> batchStockDeductions(List<StockPatchDTO> updates) throws InsufficientStockForBatchDeductionException {
        List<Product> products = this.productRepository.findAllById(updates.stream().map(StockPatchDTO::productId).collect(Collectors.toList()));

        if (stockIsAvailable(products, updates)) {
            return batchStockDeductions(products, updates);
        } else {
            throw new InsufficientStockForBatchDeductionException();
        }
    }

    public boolean stockIsAvailable(List<Product> products, List<StockPatchDTO> updates) {
        for (Product product : products) {
            for (StockPatchDTO update : updates) {
                if (product.getId().equals(update.productId())) {
                    if (product.getStock() < update.deduction()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<Product> batchStockDeductions(List<Product> products, List<StockPatchDTO> updates) {
        for (Product product : products) {
            for (StockPatchDTO update : updates) {
                if (product.getId().equals(update.productId())) {
                    if (product.getStock() >= update.deduction()) {
                        int updatedStock = product.getStock() - update.deduction();
                        product.setStock(updatedStock);
                        productRepository.save(product);
                    }
                }
            }
        }
        return products;
    }


}
