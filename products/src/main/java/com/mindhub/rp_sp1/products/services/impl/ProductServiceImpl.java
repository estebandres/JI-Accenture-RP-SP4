package com.mindhub.rp_sp1.products.services.impl;

import com.mindhub.rp_sp1.products.dtos.ProductDTO;
import com.mindhub.rp_sp1.products.dtos.PatchProductDTO;
import com.mindhub.rp_sp1.products.exceptions.ProductNotFoundException;
import com.mindhub.rp_sp1.products.models.Product;
import com.mindhub.rp_sp1.products.services.ProductService;
import com.mindhub.rp_sp1.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
